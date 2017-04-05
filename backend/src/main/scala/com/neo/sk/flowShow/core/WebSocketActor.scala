package com.neo.sk.flowShow.core

import akka.Done
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, WebSocketRequest}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import com.neo.sk.flowShow.common.AppSettings
import com.neo.sk.utils.SecureUtil

/**
  * Created by dry on 2017/4/5.
  */
trait WebSocketActor {
  def buildCode(): Flow[String, String, Any]
}

object WebSocketActor {

  private[this] val log = LoggerFactory.getLogger(getClass)

  private val websocketRef: mutable.HashMap[String, ActorRef] = mutable.HashMap()


  def create(system: ActorSystem)(implicit executor: ExecutionContext): WebSocketActor = {

    val dataWsActor = system.actorOf(Props(new Actor {

      val heartbeatTick = context.system.scheduler.schedule(5.seconds, 5.seconds, self, Tick)

      override def preStart(): Unit = {
        log.info(s"$log starts.")
      }

      override def postStop(): Unit = {
        log.info(s"$log stops.")
        heartbeatTick.cancel()
      }

      override def receive: Receive = {
        case Connect() =>
          val sn = (System.currentTimeMillis() / 1000) + SecureUtil.nonceStr(3)
          val (timestamp, nonce, signature) =
            SecureUtil.generateSignatureParameters(List(AppSettings.nyxAppId, sn), AppSettings.nyxSecureKey)

          val url = AppSettings.nyxWebsokectProtocol + AppSettings.nyxHost + ":" + AppSettings.nyxPort +
            s"/nyx/api/pulse?appId=${AppSettings.nyxAppId}&sn=$sn&timestamp=$timestamp&nonce=$nonce&signature=$signature"

          log.info("nyx  recieveDataActor  ws  url  is : " + url)
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
            case Success(_) => println(s"${self.path} connect nyx success.")
            case Failure(e) => println(s"${self.path} connect nyx failed." + e.getMessage)
          }
          closed.onComplete { x =>
            println("closed.onComplete")
            context.system.scheduler.scheduleOnce(2.minutes, self, Connect())
          }


        case RegisterWebsocket(out) =>
          println("产生一个websoc 链接" + out)
          websocketRef += out

        case DeleteWebsocket(out) =>
          websocketRef -= out

        case RealTimePersonNumberAdd(groupId, list) =>
        //TODO  每隔5分钟应该产生一个数据推送到前端

        case SecondHandleComingPeople() =>
          floorPeopleMap.foreach {
            case (floor, int) =>
              if (int > 0) {
                websocketRef.foreach {
                  each =>
                    each ! floor
                }
                floorPeopleMap.update(floor, 0)
                Thread.sleep(2000)
              }
          }
      }



    }))

    new WebSocketActor {
      override def buildCode(): Flow[String, dataMessage, Any] = {
        val in =
          Flow[String]
            .map( msg => Handle(msg))
            .to(Sink.actorRef[Event](dataWsActor, End))

        val out =
          Source.actorRef[dataMessage](5, OverflowStrategy.dropHead)
            .mapMaterializedValue(outActor => dataWsActor ! Start(outActor))

        Flow.fromSinkAndSource(in, out)
      }
    }
  }


  private sealed trait Event

  private case class Start(outActor: ActorRef) extends Event
  private case object End extends Event
  private case class Handle(msg: String) extends Event

  private trait dataMessage
  private case class Connect() extends dataMessage
  private case class RegisterWebsocket(out:ActorRef) extends dataMessage
  private case class DeleteWebsocket(out:ActorRef) extends dataMessage
  private case class RealTimePersonNumberAdd(groupId:Int,list:List[Int]) extends dataMessage
  private case class SecondHandleComingPeople() extends dataMessage
  private case object Tick extends dataMessage

}
