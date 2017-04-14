package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.stream.{Materializer, Supervision}
import akka.util.ByteStringBuilder
import org.slf4j.LoggerFactory
import akka.stream.{Materializer, OverflowStrategy}
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.neo.sk.utils.{ShootUtil, Shoot}
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest, BinaryMessage}
import com.neo.sk.flowShow.common.AppSettings
import com.neo.sk.utils.SecureUtil

/**
  * Created by whisky on 17/4/14.
  */
object WsClient {

  private [this] val log = LoggerFactory.getLogger(this.getClass)

  val strategy: Supervision.Decider = {
    case e:Exception =>
      log.error("materializer error",e)
      Supervision.Stop
  }

  def props(_system:ActorSystem,_materializer:Materializer,_executor:ExecutionContextExecutor) = Props[WsClient](
    new WsClient {
      override implicit val system:ActorSystem = _system
      override implicit val materializer:Materializer = _materializer
      override implicit val executor:ExecutionContextExecutor = _executor
    }
  )
  case object Connect

  case class SubscribeData(peer:ActorRef, name:String)

  case class PutShoots(apMac:String,shoots:List[Shoot])

}

trait WsClient extends Actor{

  import WsClient._
  private [this] val log = LoggerFactory.getLogger(this.getClass)
  private val dataBus = new DataBus()

  @throws[Exception](classOf[Exception])
  override def preStart():Unit = {
    log.info(s"${self.path} starting...")
    self ! Connect
  }

  @throws[Exception](classOf[Exception])
  override def postStop():Unit = {
    log.info(s"${self.path} stopping...")
  }

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  val incoming = Sink.foreach[Message] {
      case message: TextMessage =>
        val f = message.textStream.runFold(""){
          case (s,str) => s + str
        }
        f.map{s =>
          println(s)
        }

      case msg : BinaryMessage.Streamed =>
        //        println("receive msg from akso")
        val f = msg.dataStream.runFold(new ByteStringBuilder().result()){
          case (s,str) => s.++(str)
        }
        f.map{ s =>
          s.decodeString("UTF-8").split("\u0001").toList.flatMap{i =>
            ShootUtil.line2Shoot(i)}.groupBy(_.apMac).foreach{case (apMac,shoots) =>

            dataBus.publish((DataBus.ALL_CLASSIFY, PutShoots(apMac,shoots)))
          }
        }

      case _ =>
        println("receive unknown message")
    }

  override def receive:Receive = {
    case Connect =>

      val timestamp = System.currentTimeMillis().toString
      val nonce = SecureUtil.nonceStr(5)
      val signature = SecureUtil.generateSignature(List(timestamp, nonce, AppSettings.aksoAppId), AppSettings.aksoSecureKey)

      val url = AppSettings.aksoWebsokectProtocol + AppSettings.aksoHost + ":" + AppSettings.aksoPort +
        s"/akso/subscribe/data?appId=${AppSettings.aksoAppId}&timestamp=$timestamp&nonce=$nonce&signature=$signature"

      val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest(url))

      val ((stream,response), closed) =
        Source.actorRef(1,OverflowStrategy.fail)
          .viaMat(webSocketFlow)(Keep.both) // keep the materialized Future[WebSocketUpgradeResponse]
          .toMat(incoming)(Keep.both) // also keep the Future[Done]
          .run()
      //  val closed = webSocketFlow1.watchTermination().andThen()

      val connected = response.flatMap { upgrade =>
        if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
          Future.successful("connect success")
        } else {
          throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
        }
      } //链接建立时

      connected.onComplete(i => log.info(i.toString))
      closed.onComplete{i =>
        log.info("connect to akso closed! try again 5 minutes later")
        context.system.scheduler.scheduleOnce(5.minute, self, Connect)
      } //链接断开时

    case SubscribeData(peer,name) =>
      log.info(s"$name register data...")
      context.watch(peer)
      dataBus.subscribe(peer, DataBus.ALL_CLASSIFY+name)

  }

}
