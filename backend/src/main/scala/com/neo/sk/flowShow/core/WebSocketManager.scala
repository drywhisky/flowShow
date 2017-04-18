package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, Props}
import org.slf4j.LoggerFactory
import com.neo.sk.flowShow.core.WebSocketBus.{LeaveMac, NewMac}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by dry on 2017/4/18.
  */
object WebSocketManager {
  def props() = Props(new WebSocketManager())

  case class RegisterWsClient(peer: ActorRef)

  case class DeleterWsClient(peer: ActorRef)

}

class WebSocketManager extends Actor{

  import WebSocketManager._

  private[this] val log = LoggerFactory.getLogger(this.getClass)

  private val actorList = new ListBuffer[ActorRef]()

  @throws[Exception](classOf[Exception])
  override def preStart() = {
    log.info(s"${context.self.path}")
    log.info("webSocketManager starting...")
  }

  @throws[Exception](classOf[Exception])
  override def postStop() = {
    log.info("WebSocketManager stopping...")
  }

  override def receive = {

    case RegisterWsClient(peer) =>
      log.debug("RegisterWsClient")
      actorList += peer

    case msg@NewMac(_, _) =>
      log.debug(s"i got a msg:$msg")
      actorList.foreach( _ ! msg)

    case msg@LeaveMac(_, _) =>
      log.debug(s"i got a msg:$msg")
      actorList.foreach( _ ! msg)

    case DeleterWsClient(peer) =>
      actorList -= peer
  }

}
