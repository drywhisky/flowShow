package com.neo.sk.flowShow.core

import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorContext, ActorRef, OneForOneStrategy, Props, ReceiveTimeout, Stash, Terminated}
import akka.util.Timeout
import org.slf4j.LoggerFactory
import com.neo.sk.flowShow.models.dao.{BoxDao, GroupDao}
import com.neo.sk.flowShow.core.WsClient.SubscribeData

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by whisky on 17/4/14.
  */
object GroupManager {

  def props(wsClient: ActorRef, storeRouter: ActorRef, fileSaver: ActorRef) = Props[GroupManager](new GroupManager(wsClient, storeRouter, fileSaver))

  case class InitDone(maps: Map[String, Seq[String]], baseInfo: Map[String, (Option[Int], Option[Int])])

  case object FindMyInfo

  case class GetMyInfo(father: Option[ActorRef], durationLength: Option[Int], rssiSet: Option[Int])
}

class GroupManager(wsClient: ActorRef, storeRouter: ActorRef, fileSaver: ActorRef) extends Actor with Stash {

  import GroupManager._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val logPrefix = context.self.path

  private[this] val selfRef = context.self
  private[this] var GroupBoxMap = new mutable.HashMap[String,Seq[String]]

  implicit val timeout = Timeout(5.seconds)

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
      val groupInfo = groups.map(g => (g.groupId.toString, (Some(g.durationLength.toInt), None)))
      val boxInfo = boxs.map(b => (b.boxMac, (None, Some(b.rssiSet))))
      val baseInfo = (groupInfo ++ boxInfo).toMap
      val groupBoxMap = boxs.groupBy(_.groupId).map{ case (groupId, iter) =>
        GroupBoxMap += (groupId.toString -> iter.map(_.boxMac))
        (groupId, iter.map(_.boxMac))
      }
      //all Map[GroupId, Seq[BoxMac]]
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
      val child = context.actorOf(GroupActor.props(id, storeRouter, fileSaver), id)
      log.info(s"$logPrefix $id is starting")
      context.watch(child)
      child
    }
  }

  override def receive = init()

  def init() : Receive = {
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

  def working(relations: Map[String, Option[String]], baseInfo: Map[String, (Option[Int], Option[Int])]) : Receive = {

    case msg@FindMyInfo =>
      log.debug(s"i got a msg:$msg")
      val peer = sender()
      val father = relations.getOrElse(peer.path.name, None).map(getActor)
      val (durationLength, rssiSet) = baseInfo.getOrElse(peer.path.name, (None, None))
      peer ! GetMyInfo(father, durationLength, rssiSet)

    case Terminated(child) =>
      log.error(s"$logPrefix ${child.path.name} is dead.")

    case msg =>
      log.error(s"$logPrefix receive unknown msg: $msg")
  }





}

