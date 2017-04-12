package com.neo.sk.flowShow.core

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props, ReceiveTimeout, Stash}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import com.neo.sk.utils.NyxClient
import com.neo.sk.flowShow.common.AppSettings

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by dry on 2017/4/11.
  */
object AssistedDataActor {

  def props() = Props[AssistedDataActor](new AssistedDataActor())

  case object Init

  case class SwitchState(stateName:String, func:Receive, duration: Duration)

  case object GetResidentInfo

  case object GetRatioInfo

  case object GetBrandInfo

  case object GetFrequencyInfo

  case object GetRealTimeInfo

}

class AssistedDataActor extends Actor with Stash{

  import AssistedDataActor._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val selfRef = context.self
  private[this] val logPrefix = selfRef.path

  private[this] val InitTimeOut = 1.minutes
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
    log.info(s"$logPrefix becomes init state.")
    context.setReceiveTimeout(InitTimeOut)
    selfRef ! Init
  }

  override def postStop(): Unit = {
    log.info(s"$logPrefix stops.")
  }

  override def receive = init()

  def init() : Receive = {
    case msg@Init =>
      log.debug(s"$logPrefix got a msg:$msg.")
      switchState("idle", idle(), Duration.Undefined)

    case msg@ReceiveTimeout =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      terminate(s"no msg for $InitTimeOut when init")

    case msg =>
      log.debug(s"$logPrefix i got an unknown msg: $msg and stash.")
      stash()
  }

  def idle() : Receive = {
    case msg@GetResidentInfo =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      val peer = sender()

      val f = Future.sequence(AppSettings.groupIdNameMap.toList.map{ a =>
        NyxClient.residentsInfo(a._2).map{
          case Right(res) =>
            res
        }
      })

      f.onComplete{
        case Success(res) =>
          peer ! res
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)

        case Failure(e) =>
          log.error(s"GetResidentInfo error:$e")
          peer ! "Error"
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@GetRatioInfo =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      val peer = sender()

      val f = Future.sequence(AppSettings.groupIdNameMap.toList.map{ a =>
        NyxClient.ratioInfo(a._2).map{
          case Right(res) =>
            res
        }
      })

      f.onComplete{
        case Success(res) =>
          peer ! res
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)

        case Failure(e) =>
          log.error(s"GetRatioInfo error:$e")
          peer ! "Error"
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@GetBrandInfo =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      val peer = sender()

      val f = Future.sequence(AppSettings.groupIdNameMap.toList.map{ a =>
        NyxClient.brandsInfo(a._2).map{
          case Right(res) =>
            res
        }
      })

      f.onComplete{
        case Success(res) =>
          peer ! res
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)

        case Failure(e) =>
          log.error(s"GetBrandInfo error:$e")
          peer ! "Error"
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@GetFrequencyInfo =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      val peer = sender()

      val f = Future.sequence(AppSettings.groupIdNameMap.toList.map{ a =>
        NyxClient.frequencyInfo(a._2).map{
          case Right(res) =>
            res
        }
      })

      f.onComplete{
        case Success(res) =>
          peer ! res
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)

        case Failure(e) =>
          log.error(s"GetFrequencyInfo error:$e")
          peer ! "Error"
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@GetRealTimeInfo =>
      log.debug(s"$logPrefix i got a msg: $msg.")

      val peer = sender()

      val f = Future.sequence(AppSettings.groupIdNameMap.toList.map{ a =>
        NyxClient.realtimeDetail(a._2).map{
          case Right(res) =>
            res
        }
      })

      f.onComplete{
        case Success(res) =>
          peer ! res
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)

        case Failure(e) =>
          log.error(s"GetRealTimeInfo error:$e")
          peer ! "Error"
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)
      }
      switchState("busy", busy(), BusyTimeOut)

    case msg@SwitchState(stateName: String, func: Receive, duration: Duration) =>
      switchState(stateName, func, duration)

    case msg@ReceiveTimeout =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      terminate(s"no msg for $InitTimeOut when init")

    case msg =>
      log.debug(s"$logPrefix i got an unknown msg: $msg and stash.")
      stash()
  }

  def busy() : Receive = {
    case msg@SwitchState(stateName: String, func: Receive, duration: Duration) =>
      switchState(stateName, func, duration)

    case msg@ReceiveTimeout =>
      log.debug(s"$logPrefix i got a msg: $msg.")
      terminate(s"no msg for $InitTimeOut when init")

    case msg =>
      log.debug(s"$logPrefix i got an unknown msg: $msg and stash.")
      stash()
  }

}
