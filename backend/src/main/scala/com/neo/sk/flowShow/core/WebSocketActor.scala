package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import com.neo.sk.flowShow.Boot.receiveDataActor
import com.neo.sk.flowShow.core.ReceiveDataActor._

/**
  * Created by dry on 2017/4/5.
  */
trait WebSocketActor {
  def buildCode(): Flow[String, String, Any]
}

object WebSocketActor {

  private[this] val log = LoggerFactory.getLogger(getClass)

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

        case RegisterWebsocket(out) =>
          log.debug("产生一个websocket链接" + out)
          receiveDataActor ! Subscribe(out)

        case DeleteWebsocket(out) =>
          log.debug("断开一个websocket 链接" + out)
          receiveDataActor ! UnSubscribe(out)

        case RealTimePersonNumberAdd(groupId, list) =>
        //TODO  每隔5分钟应该产生一个数据推送到前端

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
  private case class RegisterWebsocket(out:ActorRef) extends dataMessage
  private case class DeleteWebsocket(out:ActorRef) extends dataMessage
  private case class RealTimePersonNumberAdd(groupId:Int,list:List[Int]) extends dataMessage
  private case object Tick extends dataMessage

}
