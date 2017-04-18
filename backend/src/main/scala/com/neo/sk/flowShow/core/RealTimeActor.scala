package com.neo.sk.flowShow.core

import java.io.File

import akka.actor.{Actor, Props, ReceiveTimeout, Stash}
import com.github.nscala_time.time.Imports.DateTime
import org.slf4j.LoggerFactory
import com.neo.sk.utils.FileUtil
import com.neo.sk.flowShow.common.AppSettings

import scala.collection.mutable
import com.neo.sk.flowShow.models.dao.CountDao
import com.neo.sk.flowShow.core.WebSocketManager.{LeaveMac, NewMac, PushData}

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
  case object Clean
  case object InitDone

  sealed trait State
  case object Init extends State
  case object Idle extends State

  sealed trait Data
  case object Empty extends Data

  case class GetCountDetail(groupId:String)
  case class CountDetailResult(flow:List[(Long,Int)],max:Int,total:Int,now:Int)

  case object FinishWork
}

class RealTimeActor(symbol:String) extends Actor with Stash{
  import RealTimeActor._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val logPrefix = self.path
  private[this] val selfRef = context.self

  private[this] val groupId = context.parent.path.name

  private val durationCache = collection.mutable.HashMap[String,List[(Long,Long)]]() //(clientMac) -> duration
  private var realTimeDurationCache = collection.mutable.HashMap[String,List[(Long,Long)]]() //(clientMac) -> duration
  private val realTimeMacCache = collection.mutable.HashMap[String,Long]() //mac time

  private val visitDurationLent = 2 * 60 *1000
  private val realTimeDurationLength =  9 * 60 *1000
  private val oneDurationLength = 1 * 60 * 60 *1000

  private val countCache = collection.mutable.HashMap[Long,Int]() //(time) -> count
  private var lastFileDate = ""
  private val unsureDurCache = collection.mutable.HashMap[String,List[(Long,Long)]]()
  private val realTimeUnsureDurCache = collection.mutable.HashMap[String,(Long,Long)]()

  private val reg = "[0-9]*".r
  private val needSend2Socket = true
//    if(reg.pattern.matcher(groupId).matches()) true else{
//    realTimeMacCache.clear()
//    false
//  }

  private[this] val targetDir = new File(AppSettings.tempPath + groupId + "/")
  if(!targetDir.exists){
    targetDir.mkdirs()
  }

  def countDelay = {
    val time = DateTime.now.plusMinutes(AppSettings.realTimeCountInterval).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
    val now = DateTime.now.getMillis
    time - now
  }

  def cleanDelay = {
    val time = DateTime.now.withTime(23,59,59,0)
    val now = DateTime.now
    if(time.isAfter(now)){
      time.getMillis - now.getMillis
    }else{
      time.plusDays(1).getMillis - now.getMillis
    }
  }

  def saveDelay = {
    val time = DateTime.now.plusMinutes(1).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
    val now = DateTime.now.getMillis
    time - now
  }

  private val countTask = context.system.scheduler.schedule(countDelay.millis, 5.minutes, self, CountDetailFlow)

  private val saveTempFileTask = context.system.scheduler.schedule(saveDelay.millis, 1.minutes, self, SaveTmpFile)

  private val cleanTask = context.system.scheduler.schedule(cleanDelay.millis, 24.hours, self, Clean)

  override def postStop(): Unit = {
    log.info(s"$logPrefix stops.")
    val date = DateTime.now.toString("yyyyMMdd")
    if(date != lastFileDate) {
      FileUtil.saveDuration(s"$groupId/duration-$date.txt", groupId,
        durationCache.flatMap { duration =>
          val newDuration = duration._2.filter(d => d._2 - d._1 > visitDurationLent)
          if (newDuration.nonEmpty) {
            Some(duration._1, newDuration)
          } else {
            None
          }
        }.toMap)
      lastFileDate = date
    }else{
      log.debug(s"$logPrefix file $date has already been written before")
    }
    List(
      countCache,
      durationCache    //is save count cache needed?
    ).foreach{_.clear()}

    List(
      countTask,
      saveTempFileTask,
      cleanTask
    ).foreach{_.cancel()}
  }

  def getDetailFlow = {
    val interval = 5 * 60 * 1000
    val start = DateTime.now.withTime(0,0,0,0).getMillis
    val end = DateTime.now.minusMinutes(10).getMillis
    val result = (start until end by interval).map(time => (time,0)).toMap
    val countRst = countCache.filter(_._2 != 0).toList.map{case (time,count) =>
      if(time%interval == 0)
        (time,count*5,5)
      else
        ((time - start)/interval*interval+interval+start,count,1)
    }.groupBy(_._1).map{
      i =>
        val count = i._2.map(_._2)
        val rate = i._2.map(_._3).sum
        (i._1,count.sum/rate)
    }.toList.filter(_._1 < end)
    result.++(countRst.toMap)
  }

  private def sendSocket(msg: PushData) = {
    try {
      log.debug(s"i will send $msg to WebSocketActor")
      val socket = context.system.actorSelection("/user/webSocketManager")
      socket ! msg
    }catch{
      case e:Exception =>
        log.error(s"send $msg to WebSocketActor error",e)
    }
  }

  def updateDurationCache(shoots: List[Shoot],
                          cache: collection.mutable.HashMap[String, List[(Long, Long)]],
                          intervalMillis: Int): mutable.HashMap[String, List[(Long, Long)]] = {
    shoots.groupBy(_.clientMac).foreach { case (clientMac, shootList) =>
      var realTimeShootsCache = List[Shoot]()
      val clientOpt = cache.get(clientMac)
      val oldDuration = clientOpt.getOrElse(Nil)
      val newDuration = shootList.sortBy(_.t).foldLeft(oldDuration) { case (oldShoots, shoot) =>
        if(oldShoots.isEmpty) {
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
      }
      cache.put(clientMac, newDuration)

      if (cache.nonEmpty) {
        val oldUnsureDuration = realTimeUnsureDurCache.getOrElse(clientMac,(0L,0L))
        val newUnsureDuration = realTimeShootsCache.sortBy(_.t).foldLeft(oldUnsureDuration) { case (old, shoot) =>
          if (old._1 == 0) {
            (shoot.t, shoot.t)
          } else {
            if (shoot.t - old._2 <= intervalMillis && shoot.t > old._2) {
              (old._1, shoot.t)
            } else if (shoot.t - old._2 > intervalMillis) {
              (shoot.t, shoot.t)
            } else {
              old
            }
          }
        }

        val oldDuration = cache.getOrElse(clientMac, List())
        if (newUnsureDuration._2 - newUnsureDuration._1 >= visitDurationLent){
          cache.put(clientMac, oldDuration.::(newUnsureDuration))
          if(needSend2Socket){
            val t = newUnsureDuration._2
            realTimeMacCache.put(clientMac, t)
            sendSocket(NewMac(groupId,clientMac))
          }
          realTimeUnsureDurCache.remove(clientMac)
        }else{
          realTimeUnsureDurCache.put(clientMac,newUnsureDuration)
        }
      }
    }
    cache
  }

  override def preStart(): Unit = {
    log.info(s"$logPrefix starting...")
    realTimeDurationCache ++= FileUtil.readDuration(s"$groupId/realduration.txt")

    val start = DateTime.now.withTimeAtStartOfDay().getMillis
    //TODO 需减小，要不后面都为0的话，取平均值会出错
    val end = System.currentTimeMillis()

    val initCache = (start until end by AppSettings.realTimeCountInterval*60000).map(i => (i,0)).toMap
    countCache ++= initCache
    CountDao.getCountDetailByInterval(groupId,start,end).andThen{
      case Success(res) =>
        val countList = res.map(i => (i.timestamp,i.count))
        countCache.++=(countList)
        log.info(s"$logPrefix init countCache size ${countCache.size}")
      case Failure(e) =>
        log.error(s"$logPrefix init countCache error",e)
    }.onComplete{
      case _ =>
        self ! InitDone
    }
  }

  override def receive = init()

  def init(): Receive = {
    case msg@InitDone =>
      log.info(s"$logPrefix init done")
      if(needSend2Socket) {
        context.system.scheduler.schedule(0.seconds, AppSettings.realTimeMacInterval.seconds) {
          val cur = System.currentTimeMillis()
          val leaveMac = realTimeMacCache.filter { c =>
            cur - c._2 > realTimeDurationLength
          }.keys
          realTimeMacCache.--=(leaveMac)
          if (leaveMac.nonEmpty) sendSocket(LeaveMac(groupId, leaveMac))
        }
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

  def idle() : Receive = {
    case msg@PutShoots(apMac,shoots) =>
      Future{
        log.debug(s"$logPrefix shoots:${shoots.size}")
        realTimeDurationCache = updateDurationCache(shoots, realTimeDurationCache, realTimeDurationLength)
        //        log.debug(s"$logPrefix realtimeDurationCache:$realtimeDurationCache")
      }.onComplete{
        case Success(_)=>
          selfRef ! FinishWork
        case Failure(e) =>
          log.error(s"$logPrefix working updateDurationCache error:${e.getMessage}")
          selfRef ! FinishWork
      }
      context.become(busy())

    case msg@CountDetailFlow =>
      log.debug(s"i got a msg:$msg")
      val startTime = DateTime.now.minusMinutes(60).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
      val endTime = DateTime.now.minusMillis(1).withSecondOfMinute(0).withMillisOfSecond(0).getMillis
      for(time <- startTime to endTime by AppSettings.realTimeCountInterval*60000){
        //每隔1分钟，在countcache里面更新一条数据
        countCache.put(time,0)
        val count  = realTimeDurationCache.filter{case (mac, iter) =>
          iter.exists{ case (start, end) =>
            end - start >= visitDurationLent &&
              time >= start && time < end
          }
        }.keys.size
        //          c => c._2.exists(d => (time >= d._1 && time < d._2) || (d._1 == d._2 && d._2 <= time && time - d._2 < Config.realTimeCountInterval * 60000))).keys.size
        //durationCache里面，在当前分钟停留，且总停留时间超过3分钟的人
        countCache.put(time,count)
      }
      val timeSet = (startTime to endTime by AppSettings.realTimeCountInterval*60000).toSet
      //取当天且人数不为0的时刻插入表格
      val record = countCache.filter(c => timeSet.contains(c._1) && c._2 != 0).toMap
      CountDao.addCountDetail(groupId, timeSet, record)
      log.info(s"$logPrefix add count detail size ${record.size}")

    case msg@SaveTmpFile =>
      log.debug(s"i got a msg:$msg")
      FileUtil.saveDuration(s"$groupId/realduration.txt",groupId,realTimeDurationCache.toMap)

    case msg@Clean =>
      log.debug(s"i got a msg:$msg")
      val date = DateTime.now.toString("yyyyMMdd")
      if(date != lastFileDate) {
        FileUtil.saveDuration(s"$groupId/duration-$date.txt", groupId,
          durationCache.flatMap { duration =>
            val newDuration = duration._2.filter(d => d._2 - d._1 > visitDurationLent)
            if (newDuration.nonEmpty) {
              Some(duration._1, newDuration)
            } else {
              None
            }
          }.toMap)
        lastFileDate = date
      }else{
        log.debug(s"$logPrefix file $date has already been written before")
      }
      List(
        durationCache,
        unsureDurCache,
        countCache,
        realTimeDurationCache,
        realTimeUnsureDurCache
      ).foreach(_.clear())
      new File(AppSettings.tempPath + s"$groupId/realduration.txt").getAbsoluteFile.delete()

    case msg@GetCountDetail(groupId) =>
      log.debug(s"i got a msg:$msg")
      val send = sender()
      val flow = getDetailFlow.toList.sortBy(_._1)
      val (max, now) = if(flow.isEmpty) (0, 0) else (flow.maxBy(_._2)._2, flow.last._2)
      val total = {
        realTimeDurationCache.filter(_._2.exists(d => d._2 - d._1 > visitDurationLent)).keySet.size
      }
      send ! CountDetailResult(flow,max,total,now)

    case ReceiveTimeout =>
      log.error(s"$logPrefix did not init...")
      context.setReceiveTimeout(Duration.Undefined)
      context.stop(selfRef)

    case msg =>
      log.info(s"i got a msg:$msg")
      stash()
  }

  def busy(): Receive ={
    case FinishWork =>
      unstashAll()
      context.become(idle())

    case msg =>
      stash()
  }




}
