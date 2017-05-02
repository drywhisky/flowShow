package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import com.neo.sk.flowShow.core.WebSocketManager.{LeaveMac, NewMac}
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.utils.SecureUtil
import akka.actor.PoisonPill
import com.neo.sk.flowShow.Boot.webSocketManager
import com.neo.sk.flowShow.core.RealTimeActor.GetNowInfo
import com.neo.sk.flowShow.core.WebSocketManager.{DeleterWsClient, RegisterWsClient}

/**
  * Created by dry on 2017/4/5.
  */
trait WebSocketActor {

  def buildCode(subId: String): Flow[String, WebSocketMsg, Any]
}

object WebSocketActor {

  private[this] val log = LoggerFactory.getLogger(getClass)

  def create(system: ActorSystem)(implicit executor: ExecutionContext): WebSocketActor = {

    val actorId = System.nanoTime() + SecureUtil.nonceStr(6)

    val dataWsActor = system.actorOf(Props(new Actor {

      private[this] val id = actorId

      private[this] var subscriber: ActorRef = _

      val heartbeatTick = context.system.scheduler.schedule(5.seconds, 5.seconds, self, Tick)

      override def preStart(): Unit = {
        log.info(s"$log starts.")
      }

      override def postStop(): Unit = {
        log.info(s"$log stops.")
        heartbeatTick.cancel()
      }

      override def receive: Receive = {

        case RegisterWebsocket(out, subId) =>
          log.debug("产生一个webSocket链接" + out)
          subscriber = out
          webSocketManager ! RegisterWsClient(context.self, subId)
          val realActor = context.system.actorSelection(s"/user/groupManager/$subId/RealTime")
          realActor ! GetNowInfo(subId.toLong)

        case DeleteWebsocket=>
          log.debug("断开一个webSocket 链接")
          webSocketManager ! DeleterWsClient(context.self)
          self ! PoisonPill

        case Tick =>
          subscriber ! Heartbeat(id = "heartbeat")

        case NewMac(groupId: String, mac: String, time: Long, oldOrNot: Boolean) =>
          log.info(s"$mac come in: $groupId")
          subscriber ! ComeIn(mac, time, oldOrNot)

        case LeaveMac(groupId: String, mac: Iterable[String]) =>
          log.info(s"$mac get out: $groupId")
          subscriber ! GetOut(mac.toList)

        case msg@NowInfo(_, _, _, _, _) =>
          log.info(s"i got a msg:$msg")
          subscriber ! msg

        case Handle(msg) =>
          log.info(s"$id got msg $msg.")

        case x@_ =>
          log.info(s"got unknown msg: $x")
      }

    }))

    new WebSocketActor {
      override def buildCode(subId: String): Flow[String, WebSocketMsg, Any] = {
        val in =
          Flow[String]
            .map( msg => Handle(subId))
            .to(Sink.actorRef[InnerMsg](dataWsActor, DeleteWebsocket))

        val out =
          Source.actorRef[WebSocketMsg](5, OverflowStrategy.dropHead)
            .mapMaterializedValue(outActor => dataWsActor ! RegisterWebsocket(outActor, subId))

        Flow.fromSinkAndSource(in, out)
      }
    }
  }


  private trait InnerMsg

  private case class RegisterWebsocket(out: ActorRef, subId:String) extends InnerMsg

  private case object DeleteWebsocket extends InnerMsg

  private case class Handle(msg: String) extends InnerMsg

  private case object Tick extends InnerMsg

}
