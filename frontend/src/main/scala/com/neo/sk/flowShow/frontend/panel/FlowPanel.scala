package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.Panel
import com.neo.sk.flowShow.ptcl.{WebSocketMsg, Heartbeat}
import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom.{Event, window}
import io.circe.generic.auto._
import scalatags.JsDom.short._
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{Document, MessageEvent, WebSocket}

/**
  * Created by dry on 2017/4/10.
  */
object FlowPanel extends Panel{

  private val currentDiv = div().render

  val ws = new WebSocket(getWebsocketUrl(dom.document))

  ws.onopen = { (e: Event) =>
    println(s"ws.onopen...${e.timeStamp}")
  }

  ws.onmessage = { (e: MessageEvent) =>

    val wsMsg = parse[WebSocketMsg](e.data.toString)

    wsMsg match {
      case Right(messages) =>

        messages match {
          case Heartbeat(id) =>
            println(s"i got a Heartbeat")

          case x =>
            println(s"i got a msg:$x")
        }

      case Left(e) =>
        println(s"wsMsg match fail...$e")

    }

  }

  ws.onclose = {
    (e: Event) =>
      window.alert("ws 断开")
  }

  def getWebsocketUrl(document: Document): String = {
    val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://${dom.document.location.host}/flowShow/ws/home"
  }

  def parse[T](s: String)(implicit decoder: Decoder[T]): Either[Error, T] = {
    import io.circe.parser._
    decode[T](s)
  }

  override def locationHash = ""

  override protected def build(): Div = {
    div(*.cls := "row")(
      currentDiv
    ).render
  }

}
