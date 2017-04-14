package com.neo.sk.flowShow.core

import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorContext, ActorRef, OneForOneStrategy, Props, Stash}
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

//      val relations = maps.toList.map{ case (groupId, boxMap) =>
//
//      }
  }


}

