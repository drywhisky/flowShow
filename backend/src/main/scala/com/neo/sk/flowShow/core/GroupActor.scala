package com.neo.sk.flowShow.core

import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props, ReceiveTimeout, Stash, Terminated}
import com.github.nscala_time.time.Imports.DateTime
import com.neo.sk.flowShow.common.AppSettings
import com.neo.sk.flowShow.core.GroupManager.{FindMyInfo, GetMyInfo}
import com.neo.sk.flowShow.models.dao.GroupDao
import org.slf4j.LoggerFactory
import com.neo.sk.utils.{PutShoots, Shoot}
import com.neo.sk.flowShow.models.dao.UserDao
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer
import scala.util.{Success, Failure}
import com.neo.sk.flowShow.common.Constants.ADDTYPE

/**
  * Created by whisky on 17/4/14.
  */
object GroupActor {

  def props(id:String) = Props[GroupActor](new GroupActor(id))

  case class UpdateDuration(duration: Long)

  case class UpdateRssi(rssi: Int)

  case object AddStaff

  case class UnAutoAddStaff(staffMac: String)

  case class DeleteStaff(staffMac: String)

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

  private val defaultVisitDurationLent = AppSettings.visitDurationLent.toLong
  private val defaultRssiSet = AppSettings.rssiValue

  private val defaultStaffDuration = AppSettings.staffDuration * 7 * 60 * 60 * 1000

  private val staffList = collection.mutable.ListBuffer[(String)]()

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

  val numPattern="[0-9]+".r

  private[this] val uniterType =
    if(numPattern.pattern.matcher(id).matches()) GroupType.group
    else GroupType.box

  private[this] val boxInfo = new collection.mutable.HashMap[String,Long]
  private[this] val clientData = new ListBuffer[Shoot]

  private val staticsTask = context.system.scheduler.schedule(0.seconds, 5.days, self, AddStaff)

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
    staticsTask.cancel()
  }

  def getRealTimeActor(symbol:String, historyDurationLength: Long): ActorRef = {
    val name = s"$symbol"
    context.child(name).getOrElse {
      val child = context.actorOf(RealTimeActor.props("RealTime"), "RealTime")
      context.watch(child)
      child
    }
  }

  override def receive = init()

  def init() : Receive = {
    case msg@GetMyInfo(father, fatherName, durationLength, rssiSet) =>
      log.debug(s"i receive a msg:$msg")
      context.setReceiveTimeout(Duration.Undefined)
      val duration = durationLength.getOrElse(defaultVisitDurationLent)
      val rssi = rssiSet.getOrElse(defaultRssiSet)
      if(uniterType == GroupType.group) {
        getRealTimeActor("RealTime", duration)
        UserDao.getAllStaff(id.toLong).map { r => staffList ++= r.map(_.mac) }
      }
      unstashAll()
      context.become(idle(father, fatherName, duration, rssi))

    case ReceiveTimeout =>
      log.error(s"$logPrefix did not init in 1 minute...")
      context.setReceiveTimeout(Duration.Undefined)
      context.stop(selfRef)

    case msg =>
      stash()

  }

  def idle(father: Option[ActorRef], fatherName:String, durationLength: Long, rssiSet: Int) : Receive = {
    case msg@PutShoots(boxMac, shoots) =>
      val target = if(uniterType == GroupType.group){
        for(e <- shoots){
          if(boxInfo.contains(e.apMac))
            boxInfo(e.apMac) = e.t
          else
            boxInfo += (e.apMac -> e.t)    //boxInfo (mac, time) time是最新的
        }
        shoots.filter(s => !staffList.contains(s.clientMac))
      }else
        shoots.filter(s => Math.abs((s.rssi(0) + s.rssi(1)) / 2) < rssiSet )

//      val send = if(sender().path.name == "deadLetters") "zero" else sender().path.name
//      log.debug(s"$logPrefix got data from $send.")

      val abandonSize = shoots.size - target.size
//      if(abandonSize != 0)
//        log.debug(s"$logPrefix abandon data size $abandonSize, total size ${shoots.size}.")

      if(target.nonEmpty) {
        val r1 = PutShoots(boxMac, target)
        father.foreach(_ ! r1) //send data to fathers
        if(uniterType == GroupType.group)
          getRealTimeActor("RealTime", durationLength).forward(r1)
      }

    case msg@UpdateDuration(newDuration: Long) =>
      log.debug(s"i got a msg: $msg")
      context.become(idle(father, fatherName, newDuration, rssiSet))

    case msg@UpdateRssi(newRssi: Int) =>
      log.debug(s"i got a msg: $msg")
      context.become(idle(father, fatherName, durationLength, newRssi))

    case msg@AddStaff =>
      log.debug(s"i got a msg:$msg")
      val beginDay = DateTime.lastWeek().withTime(0,0,0,0).getMillis
      val today = DateTime.now.withTime(0,0,0,0).getMillis
      UserDao.statics(beginDay, today).onComplete{
        case Success(res) =>
          res.groupBy(a => (a.cilentMac, a.groupId)).map { case (iter, item) =>
            if(item.map{r => r.outTime.get - r.inTime}.toList.sum > defaultStaffDuration){
              UserDao.addStaff(iter._1, iter._2, ADDTYPE.AUTO)
            }
          }

        case Failure(e) =>
          log.error(s"addStaff error.$e")
      }

    case msg@UnAutoAddStaff(mac) =>
      log.debug(s"i got a msg:$msg")
      staffList += mac

    case msg@DeleteStaff(mac) =>
      log.debug(s"i got a msg:$msg")
      staffList -= mac


    case Terminated(child) =>
      context.unwatch(child)

    case msg =>
      log.error(s"$logPrefix got unknown msg:$msg from ${sender().path.name}")
  }


}
