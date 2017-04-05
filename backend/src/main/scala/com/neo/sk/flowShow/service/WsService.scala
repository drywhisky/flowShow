package com.neo.sk.flowShow.service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.util.Timeout
import org.slf4j.LoggerFactory
import akka.stream.scaladsl.Flow
import akka.stream.{ActorAttributes, Materializer, Supervision}
import io.circe.generic.auto._
import io.circe.syntax._

/**
  * Created by dry on 2017/4/5.
  */
trait WsService {

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701b.service.UserWsService")

  implicit val system: ActorSystem

  implicit val materializer: Materializer

  implicit val timeout: Timeout

//  private val joinRoom = (path("joinRoom") & get & pathEndOrSingleSlash) {
//    UserAction { user =>
//      parameters(
//        'roomId.as[Long]
//      ) { case (roomId) =>
//        handleWebSocketMessages(websocketChatFlow(UserChatWebSocket.create(system).chatFlow(user, roomId)))
//      }
//    }
//  }

  def websocketChatFlow(flow: Flow[String, String, Any]): Flow[Message, Message, Any] =
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

//  val userWsRoutes = pathPrefix("userWs"){
//    joinRoom
//  }

}
