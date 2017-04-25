package com.neo.sk.flowShow.core

import akka.actor.{Actor, ActorRef, Props}
import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import scala.collection.mutable

/**
  * Created by dry on 2017/4/18.
  */
object WebSocketManager {
  def props() = Props(new WebSocketManager())

  case class RegisterWsClient(peer: ActorRef, subId:String)

  case class DeleterWsClient(peer: ActorRef)

  sealed trait PushData

  case class NewMac(groupId:String,mac:String) extends PushData

  case class LeaveMac(groupId:String,mac:Iterable[String]) extends PushData

}

class WebSocketManager extends Actor{

  import WebSocketManager._

  private[this] val log = LoggerFactory.getLogger(this.getClass)

  private val actorList = new mutable.HashMap[ActorRef, String]()

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

    case RegisterWsClient(peer, subId) =>
      log.debug("RegisterWsClient")
      actorList.put(peer, subId)

    case msg@NewMac(groupId, _) =>
      log.debug(s"i got a msg:$msg")
      actorList.filter(_._2 == groupId).foreach( _._1 ! msg)

    case msg@LeaveMac(groupId, _) =>
      log.debug(s"i got a msg:$msg")
      actorList.filter(_._2 == groupId)foreach( _._1 ! msg)

    case DeleterWsClient(peer) =>
      actorList.remove(peer)
  }

}
