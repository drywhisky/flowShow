package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, Props, ReceiveTimeout, Stash, Terminated}
import com.neo.sk.flowShow.common.AppSettings
import com.neo.sk.flowShow.core.GroupManager.{FindMyInfo, GetMyInfo}
import org.slf4j.LoggerFactory
import com.neo.sk.utils.{PutShoots, Shoot}
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer

/**
  * Created by whisky on 17/4/14.
  */
object GroupActor {

  def props(id:String) = Props[GroupActor](new GroupActor(id))

}

object StatisticSymbol {
  val realTime = "RealTime"
}

case object GroupType{
  val box = 0
  val group = 1
}


class GroupActor(id:String) extends Actor with Stash{

  import GroupActor._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val logPrefix = context.self.path

  private[this] val selfRef = context.self

  private val visitDurationLent = 2 * 60 *1000
  private val realTimeDurationLength =  9 * 60 *1000
  private val oneDurationLength = 1 * 60 * 60 *1000

  val numPattern="[0-9]+".r

  private[this] val uniterType =
    if(numPattern.pattern.matcher(id).matches()) GroupType.group
    else GroupType.box

  private[this] val boxInfo = new collection.mutable.HashMap[String,Long]
  private[this] val clientData = new ListBuffer[Shoot]

  private[this] def terminate(cause:String) = {
    log.info(s"$logPrefix will terminate because $cause.")
    context.stop(selfRef)
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"$logPrefix starts.")
    context.setReceiveTimeout(1.minutes)
    context.parent ! FindMyInfo
  }

  override def postStop(): Unit = {
    log.info(s"$logPrefix stops.")
  }

  def getRealTimeActor(symbol:String, historyDurationLength: Int): ActorRef = {
    val name = s"$symbol"
    context.child(name).getOrElse {
      val actor = RealTimeActor.props(symbol)
      val child = context.actorOf(actor, symbol)
      log.info(s"$logPrefix $name is starting.")
      context.watch(child)
      child
    }
  }

  override def receive = init()

  def init() : Receive = {
    case msg@GetMyInfo(father, durationLength, rssiSet) =>
      log.debug(s"i receive a msg:$msg")
      context.setReceiveTimeout(Duration.Undefined)
      val duration = durationLength.getOrElse(visitDurationLent)
      val rssi = rssiSet.getOrElse(AppSettings.rssiValue)
      getRealTimeActor(StatisticSymbol.realTime, duration)
      unstashAll()
      context.become(idle(father, duration, rssi))

    case ReceiveTimeout =>
      log.error(s"$logPrefix did not init in 1 minute...")
      context.setReceiveTimeout(Duration.Undefined)
      context.stop(selfRef)

    case msg =>
      stash()

  }

  def idle(father: Option[ActorRef], durationLength: Int, rssiSet: Int) : Receive = {
    case msg@PutShoots(boxMac, shoots) =>
//      laseConnectTime = System.currentTimeMillis()
//      dataVolume += shoots.size
      //      log.info(s"$unitId receive a shoot")
      //      log.info(s"$dataVolume is dataVolume")
      log.debug(s"i got a msg:$msg")
      if(uniterType != GroupType.box){
        for(e <- shoots){
          if(boxInfo.contains(e.apMac))
            boxInfo(e.apMac) = e.t
          else
            boxInfo += (e.apMac -> e.t)
        }
      }else
        clientData ++= shoots

      val send = if(sender().path.name == "deadLetters") "zero" else sender().path.name
      //      log.debug(s"$logPrefix got data from $send.")
      val target = shoots.filter(s => Math.abs(s.rssi(0)) < rssiSet)

      val abandonSize = shoots.size - target.size
      if(abandonSize != 0) log.debug(s"$logPrefix abandon data size ${abandonSize}, total size ${shoots.size}.")

      if(target.nonEmpty) {
        val r1 = PutShoots(boxMac, target)
        father.foreach(_ ! r1) //send data to fathers
        getRealTimeActor(id, durationLength).forward(r1)
      }

    case Terminated(child) =>
      context.unwatch(child)

    case msg =>
      log.error(s"$logPrefix got unknown msg:$msg from ${sender().path.name}")
  }


}
