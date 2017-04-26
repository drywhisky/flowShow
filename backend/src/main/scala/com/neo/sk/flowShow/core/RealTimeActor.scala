package com.neo.sk.flowShow.core

import java.io.File

import akka.actor.{Actor, ActorContext, Props, ReceiveTimeout, Stash}
import com.github.nscala_time.time.Imports.DateTime
import org.slf4j.LoggerFactory
import com.neo.sk.utils.FileUtil
import com.neo.sk.flowShow.common.AppSettings

import scala.collection.mutable
import com.neo.sk.flowShow.models.dao.CountDao
import com.neo.sk.flowShow.core.WebSocketManager.{LeaveMac, NewMac, PushData}
import com.neo.sk.flowShow.ptcl.NowInfo
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.utils.{PutShoots, Shoot}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by dry on 2017/4/17.
  */
object RealTimeActor {

  def props(symbol:String) = Props(new RealTimeActor(symbol))

  case object CountDetailFlow
  case object SaveTmpFile
  case object InitDone
  case class  GetNowInfo(groupId: Long)

  sealed trait State
  case object Init extends State
  case object Idle extends State

  sealed trait Data
  case object Empty extends Data

  case class GetCountDetail(groupId:String)
  case class CountDetailResult(flow:List[(Long,Int)],max:Int,total:Int,now:Int)

  case object FinishWork
}

class RealTimeActor(fatherName:String) extends Actor with Stash{
  import RealTimeActor._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val logPrefix = self.path
  private[this] val selfRef = context.self

  private[this] val groupId = context.parent.path.name

  //(clientMac) -> duration(startTime, endTime)
  private var realTimeDurationCache = collection.mutable.HashMap[String, List[(Long, Long)]]()

  //mac -> time
  private val realTimeMacCache = collection.mutable.HashMap[String, Long]()

  //mac -> times
  private val clientMacIn = collection.mutable.HashMap[String, Int]()
  private val clientMacOut = collection.mutable.HashMap[String, Int]()

  private val visitDurationLent = AppSettings.visitDurationLent
  //持续两分钟收到算进店
  private val realTimeDurationLength = 5 * 60 * 1000 ///五分钟没有收到上报数据算离开

  private val countCache = collection.mutable.HashMap[Long, Int]()

  //(time) -> count
  private val realTimeUnsureDurCache = collection.mutable.HashMap[String, (Long, Long)]()

  private val reg = "[0-9]*".r
  private val needSend2Socket = if (reg.pattern.matcher(groupId).matches()) true else {
    realTimeMacCache.clear()
    false
  }

  private[this] val targetDir = new File(AppSettings.tempPath + groupId + "/")
  if (!targetDir.exists) {
    targetDir.mkdirs()
  }

  def countDelay = {
    val time = DateTime.now.plusMinutes(AppSettings.realTimeCountInterval).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
    val now = DateTime.now.getMillis
    time - now
  }

  def cleanDelay = {
    val time = DateTime.now.withTime(23, 59, 59, 0)
    val now = DateTime.now
    if (time.isAfter(now)) {
      time.getMillis - now.getMillis
    } else {
      time.plusDays(1).getMillis - now.getMillis
    }
  }

  def saveDelay = {
    val time = DateTime.now.plusMinutes(1).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
    val now = DateTime.now.getMillis
    time - now
  }

  private val countTask = context.system.scheduler.schedule(countDelay.millis, 10.seconds, self, CountDetailFlow)

  private val saveTempFileTask = context.system.scheduler.schedule(saveDelay.millis, 1.minutes, self, SaveTmpFile)

  override def postStop(): Unit = {
    log.info(s"$logPrefix stops.")

    countCache.clear()

    List(
      countTask,
      saveTempFileTask
    ).foreach {
      _.cancel()
    }
  }

  def getDetailFlow = {
    val interval = 5 * 60 * 1000
    //五分钟为界限
    val start = DateTime.now.withTime(0, 0, 0, 0).getMillis
    val end = DateTime.now.minusMinutes(10).getMillis
    val result = (start until end by interval).map(time => (time, 0)).toMap
    val countRst = countCache.filter(_._2 != 0).toList.map { case (time, count) =>
      if (time % interval == 0)
        (time, count * 5, 5)
      else
        ((time - start) / interval * interval + interval + start, count, 1)
    }.groupBy(_._1).map {
      i =>
        val count = i._2.map(_._2)
        val rate = i._2.map(_._3).sum
        (i._1, count.sum / rate)
    }.toList.filter(_._1 < end)
    result.++(countRst.toMap)
  }

  private def sendSocket(msg: PushData) = {
    try {
      val socket = context.system.actorSelection("/user/webSocketManager")
      socket ! msg
    } catch {
      case e: Exception =>
        log.error(s"send $msg to WebSocketActor error", e)
    }
  }

  def updateDurationCache(shoots: List[Shoot],
                          cache: mutable.HashMap[String, List[(Long, Long)]],
                          intervalMillis: Int): mutable.HashMap[String, List[(Long, Long)]] = {
    shoots.groupBy(_.clientMac).foreach { case (clientMac, shootList) =>
      var realTimeShootsCache = List[Shoot]()
      val clientOpt = cache.get(clientMac)
      val oldDuration = clientOpt.getOrElse(Nil)
      val newDuration = shootList.sortBy(_.t).foldLeft(oldDuration) { case (oldShoots, shoot) =>
        if (oldShoots.isEmpty) {
          realTimeShootsCache = shootList
          (shoot.t, shoot.t) :: oldShoots
        } else {
          val (startTime, endTime) = oldShoots.head
          if (shoot.t <= endTime) {
            oldShoots //time illegal, dropped
          } else if (shoot.t - endTime < intervalMillis) {
            //it is ok to combine
            realTimeShootsCache = realTimeShootsCache.::(shoot)
            (startTime, shoot.t) :: oldShoots.tail
          } else {
            (shoot.t, shoot.t) :: oldShoots //directly add
          }
        }
      } //每五分钟更新一次（starttime, endtime）的list
      cache.put(clientMac, newDuration)

      if (cache.nonEmpty) {
        val oldUnsureDuration = realTimeUnsureDurCache.getOrElse(clientMac, (0L, 0L))
        val newUnsureDuration = realTimeShootsCache.sortBy(_.t).foldLeft(oldUnsureDuration) { case (old, shoot) =>
          if (old._1 == 0) {
            (shoot.t, shoot.t) //第一次取
          } else {
            if (shoot.t - old._2 <= intervalMillis && shoot.t > old._2) {
              (old._1, shoot.t) //如果时间比较新则更新
            } else if (shoot.t - old._2 > intervalMillis) {
              (shoot.t, shoot.t) //重新计算
            } else {
              old
            }
          }
        }

        val oldDuration = cache.getOrElse(clientMac, List())
        if (newUnsureDuration._2 - newUnsureDuration._1 >= visitDurationLent) {
          cache.put(clientMac, oldDuration.::(newUnsureDuration))
          if (needSend2Socket && realTimeMacCache.get(clientMac).isEmpty) {
            sendSocket(NewMac(groupId, clientMac, newUnsureDuration._1))
            clientMacIn.put(clientMac, clientMacIn.getOrElse(clientMac, 0) + 1)
            CountDao.userIn(clientMac, groupId.toLong, System.currentTimeMillis())
          }
          realTimeMacCache.put(clientMac, newUnsureDuration._2)
        } else {
          realTimeUnsureDurCache.put(clientMac, newUnsureDuration)
        }
      }
    }
    cache
  }

  override def preStart(): Unit = {
    log.info(s"$logPrefix starting...")
    realTimeDurationCache ++= FileUtil.readDuration(s"$groupId/realduration.txt")

    val start = DateTime.now.withTimeAtStartOfDay().getMillis
    val end = System.currentTimeMillis()

    CountDao.getCountDetailByInterval(groupId, start, end).andThen {
      case Success(res) =>
        val countList = res.map(i => (i.timestamp, i.count))
        countCache.++=(countList)
        log.info(s"$logPrefix init countCache size ${countCache.size}")
      case Failure(e) =>
        log.error(s"$logPrefix init countCache error", e)
    }.onComplete {
      case _ =>
        self ! InitDone
    }
  }

  override def receive = init()

  def init(): Receive = {
    case msg@InitDone =>
      log.info(s"$logPrefix init done")
      if (needSend2Socket) {
        context.system.scheduler.schedule(0.seconds, AppSettings.realTimeMacInterval.seconds) {
          val cur = System.currentTimeMillis()
          val leaveMac = realTimeMacCache.filter { c =>
            cur - c._2 > realTimeDurationLength
          }.keys
          realTimeMacCache.--=(leaveMac)
          realTimeUnsureDurCache.--=(leaveMac)
          if (leaveMac.nonEmpty) {
            sendSocket(LeaveMac(groupId, leaveMac))
            leaveMac.foreach { i =>
              realTimeUnsureDurCache.remove(i)
              clientMacOut.put(i, clientMacOut.getOrElse(i, 0))
              CountDao.userOut(i, groupId.toLong, System.currentTimeMillis())
            }
          }
        } //一秒钟检查一次是否离开
      }
      unstashAll()
      context.become(idle())

    case ReceiveTimeout =>
      log.error(s"$logPrefix did not init...")
      context.setReceiveTimeout(Duration.Undefined)
      context.stop(selfRef)

    case msg =>
      log.info(s"i got a msg:$msg")
      stash()
  }

  def idle(): Receive = {
    case msg@PutShoots(apMac, shoots) =>
      Future {
        log.debug(s"$logPrefix shoots:${shoots.size}")
        realTimeDurationCache = updateDurationCache(shoots, realTimeDurationCache, realTimeDurationLength)
      }.onComplete {
        case Success(_) =>
          selfRef ! FinishWork
        case Failure(e) =>
          log.error(s"$logPrefix working updateDurationCache error:${e.getMessage}")
          selfRef ! FinishWork
      }
      context.become(busy())

    case msg@CountDetailFlow => ///10s启动一次.每隔10秒，在countcache里面更新一条数据
      log.debug(s"i got a msg:$msg")
      val time = DateTime.now.minusSeconds(10).getMillis
        val count = realTimeDurationCache.filter { case (mac, iter) =>
          iter.exists { case (start, end) =>
            end - start >= visitDurationLent &&
              time >= start && time < end
          }
        }.keys.size
        countCache.put(time, count)
      //取当天且人数不为0的时刻插入表格
      val record = countCache.filter(c => c._1 == time && c._2 != 0).head
      CountDao.addCountDetail(groupId, record)

    case msg@SaveTmpFile =>
      log.debug(s"i got a msg:$msg")
      FileUtil.saveDuration(s"$groupId/realduration.txt", groupId, realTimeDurationCache.toMap)

    case msg@GetCountDetail(_) =>
      log.debug(s"i got a msg:$msg")
      val send = sender()
      val flow = getDetailFlow.toList.sortBy(_._1)
      val (max, now) = if (flow.isEmpty) (0, 0) else (flow.maxBy(_._2)._2, flow.last._2)
      val total = {
        realTimeDurationCache.filter(_._2.exists(d => d._2 - d._1 > visitDurationLent)).keySet.size
      }
      send ! CountDetailResult(flow, max, total, now)

    case msg@GetNowInfo(_) =>
      log.debug(s"i got a msg:$msg")
      val peer = sender()
      val inSum = clientMacIn.values.sum
      val outSum = clientMacOut.values.sum
      val online = {
        if (realTimeUnsureDurCache.nonEmpty) {
          realTimeMacCache.toList.map { i =>
            val tmp = realTimeUnsureDurCache.get(i._1).head
            (i._1, tmp._2)
          }
        }
        else List()
      }
      peer ! NowInfo(online, inSum, outSum)

    case ReceiveTimeout =>
      log.error(s"$logPrefix did not init...")
      context.setReceiveTimeout(Duration.Undefined)
      context.stop(selfRef)

    case msg =>
      log.info(s"i got a msg:$msg")
      stash()
  }

  def busy(): Receive = {
    case FinishWork =>
      unstashAll()
      context.become(idle())

    case msg =>
      stash()
  }


}
