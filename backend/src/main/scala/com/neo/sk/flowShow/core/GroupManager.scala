package com.neo.sk.flowShow.core

import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props, ReceiveTimeout, Stash, Terminated}
import akka.util.Timeout
import org.slf4j.LoggerFactory
import com.neo.sk.flowShow.models.dao.{BoxDao, GroupDao}
import com.neo.sk.flowShow.core.WsClient.SubscribeData
import com.neo.sk.utils.{PutShoots, Shoot}
import com.neo.sk.flowShow.core.GroupActor._
import scala.util.{Failure, Success}
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.flowShow.ptcl._

/**
  * Created by whisky on 17/4/14.
  */
object GroupManager {

  def props(wsClient: ActorRef) = Props[GroupManager](new GroupManager(wsClient))

  case class SwitchState(stateName:String, func:Receive, duration: Duration)

  case class InitDone(maps: Map[String, Seq[String]], baseInfo: Map[String, (Option[Long], Option[Int])])

  case object FindMyInfo

  case class GetMyInfo(father: Option[ActorRef], fatherName:String, durationLength: Option[Long], rssiSet: Option[Int])

  case class AddGroupMsg(info: AddGroup, userId: Long)

  case class AddBoxMsg(info: AddBox, userId: Long)

  case class ModifyGroupMsg(info: ModifyGroup)

  case class ModifyBoxMsg(info: ModifyBox)
}


class GroupManager(wsClient: ActorRef) extends Actor with Stash {

  import GroupManager._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val logPrefix = context.self.path

  private[this] val selfRef = context.self

  ///盒子靠groupId分组做缓存(groupId, Seq[boxMac])
  private[this] var GroupBoxMap = new mutable.HashMap[String,Seq[String]]

  implicit val timeout = Timeout(5.seconds)

  private[this] val BusyTimeOut = 1.minutes

  private[this] def switchState(stateName:String, func:Receive, duration: Duration) ={
    log.debug(s"$logPrefix becomes $stateName state.")
    unstashAll()
    context.become(func)
    context.setReceiveTimeout(duration)
  }

  private[this] def terminate(cause:String) = {
    log.info(s"$logPrefix will terminate because $cause.")
    context.stop(selfRef)
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"$logPrefix starts.")
    context.setReceiveTimeout(5.minutes)
    for {
      groups <- GroupDao.listDistributedGroups
      boxs <- BoxDao.listDistributedBoxs
    } yield {
      val groupInfo = groups.map(g => (g.groupId.toString, (Some(g.durationLength), None)))
      val boxInfo = boxs.map(b => (b.boxMac, (None, Some(b.rssiSet))))
      val baseInfo = (groupInfo ++ boxInfo).toMap
      val groupBoxMap = boxs.groupBy(_.groupId).map{ case (groupId, iter) =>
        GroupBoxMap += (groupId.toString -> iter.map(_.boxMac))
        (groupId, iter.map(_.boxMac))
      }
      val maps = groups.groupBy(_.groupId).map { case (groupId, iter) =>
        (groupId.toString, groupBoxMap.getOrElse(groupId, Nil))
      }
      //Map[GroupId, BoxMac]
      selfRef ! InitDone(maps, baseInfo)
    }
  }

  override def postStop(): Unit = {
    log.info(s"$logPrefix stops.")
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.minutes) {
      case _: ArithmeticException => Resume
      case e: Exception =>
        log.error(s"$logPrefix child dead abnormal", e)
        Restart

      case msg =>
        log.error(s"$logPrefix received unknow $msg")
        Restart
    }


  def getActor(id: String) : ActorRef = {
    context.child(id).getOrElse {
      val child = context.actorOf(GroupActor.props(id), id)
      context.watch(child)
      child
    }
  }

  def getChildActor(path:String) = {
    context.child(path)
  }

  override def receive = init()

  def init() : Receive = {
    //maps: [String, Seq[String]] baseInfo:[String:groupId/boxMac, (Option[Int]:durationLength, Option[Int]:rssiSet)]
    case InitDone(maps, baseInfo) =>
      context.setReceiveTimeout(Duration.Undefined)
      val group = maps.keys
      group.foreach(getActor)
      val box = maps.values.flatten
      box.foreach{ boxMac =>
        val actor = getActor(boxMac)
        wsClient ! SubscribeData(actor, boxMac)
      }

      val relations = maps.toList.flatMap{ case (groupId, boxMap) =>
        val g1 = (groupId, None)  // group with no father
        val b1 = boxMap.map{( _ , Some(groupId))} //box with group father
        b1 :+ g1
      }.toMap
      unstashAll()
      context.become(working(relations, baseInfo))

    case ReceiveTimeout =>
      log.error(s"$logPrefix did not init in 1 minute...")
      context.setReceiveTimeout(Duration.Undefined)
      context.stop(selfRef)

    case msg =>
      log.info(s"i got a msg:$msg")
      stash()
  }

  def working(relations: Map[String, Option[String]], baseInfo: Map[String, (Option[Long], Option[Int])]) : Receive = {

    case msg@FindMyInfo =>
      log.debug(s"i got a msg:$msg")
      val peer = sender()
      val father = relations.getOrElse(peer.path.name, None).map(getActor)
      val fatherName =  relations.getOrElse(peer.path.name, peer.path.name).toString
      val (durationLength, rssiSet) = baseInfo.getOrElse(peer.path.name, (None, None))
      peer ! GetMyInfo(father, fatherName, durationLength, rssiSet)

    case msg@AddGroupMsg(info, userId) =>
      log.debug(s"i got a msg $msg")
      val peer = sender()
      val time = System.currentTimeMillis()
      GroupDao.addGroup(info.name, info.durationLength, userId, time, info.map, info.scala, info.width, info.height).onComplete{
        case Success(id) =>
          peer ! (id, time)
          selfRef ! SwitchState("working", working(relations.+((id.toString, None)), baseInfo.+((id.toString, (Some(info.durationLength), None)))), Duration.Undefined)
          getActor(id.toString)

        case Failure(e) =>
          log.debug(s"AddGroup error in database.$e")
          peer ! "Error"
          selfRef ! SwitchState("working", working(relations, baseInfo), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@AddBoxMsg(info, userId) =>
      log.debug(s"i got a msg $msg")
      val peer = sender()
      val time = System.currentTimeMillis()
      BoxDao.addBox(info.name, info.mac, info.rssi, userId, info.groupId, time, info.x, info.y).onComplete{
        case Success(id) =>
          if(id != -1) {
            peer ! (id, time)
            selfRef ! SwitchState("working", working(relations.+((info.mac, Some(info.groupId.toString))), baseInfo.+((id.toString, (None, Some(info.rssi))))), Duration.Undefined)
            //暂时不给盒子的group father发消息
            getActor(info.mac)
          }else{
            log.debug(s"AddBox error in database.${info.mac} repeated.")
            peer ! "Error"
            selfRef ! SwitchState("working", working(relations, baseInfo), Duration.Undefined)
          }

        case Failure(e) =>
          log.debug(s"AddBox error in database.$e")
          peer ! "Error"
          selfRef ! SwitchState("working", working(relations, baseInfo), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@ModifyGroupMsg(info) =>
      log.debug(s"i got a msg $msg")
      val peer = sender()
      GroupDao.modifyGroup(info.id, info.name, info.durationLength).onComplete{
        case Success(_) =>
          peer ! "OK"
          getActor(info.id.toString) ! UpdateDuration(info.durationLength)
          val newBaseInfo = baseInfo.updated(info.id.toString, (Some(info.durationLength), None))
          selfRef ! SwitchState("working", working(relations, newBaseInfo), Duration.Undefined)

        case Failure(e) =>
          log.debug(s"ModifyGroup error in database.$e")
          peer ! "Error"
          selfRef ! SwitchState("working", working(relations, baseInfo), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@ModifyBoxMsg(info) =>
      log.debug(s"i got a msg $msg")
      val peer = sender()
      BoxDao.modifyBox(info.id, info.name, info.rssi).onComplete{
        case Success(_) =>
          peer ! "OK"
          getActor(info.mac) ! UpdateRssi(info.rssi)
          val newBaseInfo = baseInfo.updated(info.id.toString, (None, Some(info.rssi)))
          selfRef ! SwitchState("working", working(relations, newBaseInfo), Duration.Undefined)

        case Failure(e) =>
          log.debug(s"ModifyBox error in database.$e")
          peer ! "Error"
          selfRef ! SwitchState("working", working(relations, baseInfo), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case Terminated(child) =>
      log.error(s"$logPrefix ${child.path.name} is dead.")

    case msg =>
      log.error(s"$logPrefix receive unknown msg: $msg")
  }

  def busy() : Receive = {
    case msg@SwitchState(stateName: String, func: Receive, duration: Duration) =>
      switchState(stateName, func, duration)

    case msg =>
      log.debug(s"$logPrefix i got an unknown msg: $msg and stash.")
      stash()
  }





}

