package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.Panel
import com.neo.sk.flowShow.ptcl.{ComeIn, GetOut, Heartbeat, WebSocketMsg}
import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom.{Event, MouseEvent, window}
import io.circe.generic.auto._

import scalatags.JsDom.short._
import org.scalajs.dom.html.{Div, IFrame}
import org.scalajs.dom.raw.{Document, MessageEvent, WebSocket}
import com.neo.sk.flowShow.frontend.utils.Shortcut

import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.flowShow.frontend.utils.Http
import com.neo.sk.flowShow.frontend.Routes

import scala.collection.mutable
import scalatags.JsDom.svgTags.{g, image}

/**
  * Created by whisky on 17/4/25.
  */
object StaticPanel extends Panel{

  private val staticDiv = div(*.width := "100%", *.height := "100%").render

  private val rangeIndex = select(*.width := "150px", *.color := "black", *.height := "30px", *.marginRight := "10px").render

  private val searchByDateButton = button(*.cls := "btn btn-default")("查询").render

  private val GroupMap = mutable.HashMap[String, Group]()

  private val container = g().render

  private val searchByIdIncome =
    div(
      form(*.cls := "form-inline", *.marginLeft := "8%")(
        div(*.cls := "form-group")(
          rangeIndex
        ),
        searchByDateButton
      )
    ).render

  searchByDateButton.onclick = {
    e: MouseEvent =>
      e.preventDefault()
      val roomName = rangeIndex.value
      val group = GroupMap.get(roomName).head
      openWs(group.id.toString)
  }

  def openWs(subId: String) = {

    val ws = new WebSocket(getWebsocketUrl(dom.document, subId))

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

            case msg@ComeIn(num) =>
              println(s"comeIn.i got a msg:$msg")

            case msg@GetOut(num) =>
              println(s"i got a msg:$msg")

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

  }

  def getWebsocketUrl(document: Document, subId: String): String = {
    val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://${dom.document.location.host}/flowShow/ws/home?subId=$subId"
  }

  def parse[T](s: String)(implicit decoder: Decoder[T]): Either[Error, T] = {
    import io.circe.parser._
    decode[T](s)
  }

  def makeGroupsSelects(list: List[Group]) = {

    def makeGroupSelects(group: Group) = {
      rangeIndex.appendChild(option(s"${group.name}").render)
    }

    list.map(data => makeGroupSelects(data))

  }

  def getGroupList() = {
    Http.getAndParse[GroupsRsp](Routes.getGroups).map { rsp =>
      if (rsp.errCode == 0) {
        GroupMap ++= rsp.data.map(g => (g.name, g))
        makeGroupsSelects(rsp.data)
      }
    }
  }

  override def locationHash = ""

  override protected def build(): Div = {
    getGroupList()
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-12", *.textAlign := "center")(
          h1(
            span(*.cls := "artpip-highlight", *.color := "#13C5E4")("区域实时客流呈现")
          )
        )
      ),
      searchByIdIncome,
      div(*.cls := "row", *.width := "100%", *.height := "488px")(
        staticDiv
      )
    ).render
  }

}
