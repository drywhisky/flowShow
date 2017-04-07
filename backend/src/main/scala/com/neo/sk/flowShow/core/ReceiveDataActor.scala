package com.neo.sk.flowShow.core

import akka.Done
import akka.actor._
import akka.actor.Actor._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.util.Timeout
import com.neo.sk.flowShow.common.AppSettings
import com.neo.sk.utils.SecureUtil
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
/**
  * Created by dry on 2017/4/5.
  */
object ReceiveDataActor {

  def props() = Props[ReceiveDataActor](new ReceiveDataActor())

  //internal
  case object Init

  case object Connect

  case class Subscribe(actorRef: ActorRef)

  case class UnSubscribe(actorRef: ActorRef)

  case class SwitchState(stateName: String, func: Receive, duration: Duration)

}

class ReceiveDataActor extends Actor with Stash {
  import ReceiveDataActor._

  private[this] val log = LoggerFactory.getLogger(getClass)

  private[this] val selfRef = context.self
  private[this] val logPrefix = selfRef.path

  private[this] val InitTimeOut = 1.minutes
  private[this] val BusyTimeOut = 1.minutes

  implicit val timeout: Timeout = 1.minutes

  private val dataBus = new DataBus()

  private[this] def switchState(stateName: String, func: Receive, duration: Duration) = {
    log.debug(s"$logPrefix becomes $stateName state.")
    unstashAll()
    context.become(func)
    context.setReceiveTimeout(duration)
  }

  private[this] def terminate(cause: String) = {
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

  val incoming: Sink[Message, Future[Done]] =
    Sink.foreach[Message] {
      case msg: TextMessage.Strict=>
        val str = msg.text
        val arr = str.split("#")
        // groupId, type(1 is come , 0 is leave) , num(come always be 1)
        //        cacheMap.leaveNumberOperator(true,1)

        if(arr(1).toInt==1){
          val floor = if(arr(0)=="13"){
            "0"
          }else if(arr(0)=="14"){
            "2"
          } else if(arr(0)=="12"){
            "4"
          }else{
            "6"
          }

          dataBus.publish((arr(1), floor))
        }

      case msg =>
        log.info(s" receive unknown msg: " + msg)
    }

  override def receive = init()

  def init(): Receive = {
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

  def idle(): Receive = {
    case msg@Connect =>
      log.debug(s"$logPrefix got a msg:$msg.")
      val sn = (System.currentTimeMillis() / 1000) + SecureUtil.nonceStr(3)
      val (timestamp, nonce, signature) =
        SecureUtil.generateSignatureParameters(List(AppSettings.nyxAppId, sn), AppSettings.nyxSecureKey)

      val url = AppSettings.nyxWebsokectProtocol + AppSettings.nyxHost + ":" + AppSettings.nyxPort +
        s"/nyx/api/pulse?appId=${AppSettings.nyxAppId}&sn=$sn&timestamp=$timestamp&nonce=$nonce&signature=$signature"

      log.info("nyx recieveDataActor  ws  url  is : " + url)
      val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest(url))

      val (upgradeResponse, closed) =
        Source.maybe[Message]
          .viaMat(webSocketFlow)(Keep.right) // keep the materialized Future[WebSocketUpgradeResponse]
          .toMat(incoming)(Keep.both) // also keep the Future[Done]
          .run()

      val connected = upgradeResponse.flatMap { upgrade =>
        if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
          Future.successful(Done)
        } else {
          throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
        }
      }

      connected.onComplete {
        case Success(_) =>
          log.debug(s"${self.path} connect nyx success.")

        case Failure(e) =>
          log.debug(s"${self.path} connect nyx failed." + e.getMessage)
      }

      closed.onComplete { x =>
        log.debug("closed.onComplete")
        context.system.scheduler.scheduleOnce(2.minutes, self, Connect)
      }

    case msg@Subscribe(actorRef) =>
      log.debug(s"i got a msg: $msg")
      dataBus.subscribe(actorRef, "")

    case msg@UnSubscribe(actorRef) =>
      log.debug(s"i got a msg: $msg")
      dataBus.unsubscribe(actorRef)

    case msg =>
      log.debug(s"$logPrefix i got an unknown msg: $msg and stash.")
      stash()

  }

}


