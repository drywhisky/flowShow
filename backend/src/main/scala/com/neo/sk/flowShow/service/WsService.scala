package com.neo.sk.flowShow.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.util.Timeout
import org.slf4j.LoggerFactory
import akka.stream.scaladsl.Flow
import akka.stream.{ActorAttributes, Materializer, Supervision}
import io.circe.syntax._
import io.circe.generic.auto._
import com.neo.sk.utils.CirceSupport
import com.neo.sk.flowShow.core.WebSocketActor
import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.flowShow.ptcl.WebSocketMsg


/**
  * Created by dry on 2017/4/5.
  */
trait WsService extends ServiceUtils with SessionBase with CirceSupport{

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701b.service.WsService")

  implicit val system: ActorSystem

  implicit val materializer: Materializer

  implicit val timeout: Timeout

  private val home = (path("home") & get & pathEndOrSingleSlash) {
    handleWebSocketMessages(websocketChatFlow(WebSocketActor.create(system).buildCode()))
  }

  def websocketChatFlow(flow: Flow[String, WebSocketMsg, Any]): Flow[Message, Message, Any] =
    Flow[Message]
      .collect {
        case TextMessage.Strict(msg) =>
          log.debug(s"msg from webSocket: $msg")
          msg
      }
      .via(flow)
      .map { msg => TextMessage.Strict(msg.asJson.noSpaces)
      }.withAttributes(ActorAttributes.supervisionStrategy(decider))

  val decider: Supervision.Decider = {
    e: Throwable =>
      e.printStackTrace()
      println(s"WS stream failed with $e")
      Supervision.Resume
  }

  val wsRoutes = pathPrefix("ws"){
    home
  }

}
