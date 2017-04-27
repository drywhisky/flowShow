package com.neo.sk.flowShow.frontend.panel

import java.util.Date

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
  * Created by whisky on 17/4/24.
  */
object AreaPanel extends Panel {

  private val areaDiv = div(*.cls := "row")().render

  private val onLineDiv = div(*.cls := "row")().render

  private val rangeIndex = select(*.width := "150px", *.color := "black", *.height := "30px", *.marginRight := "10px").render

  private val searchByDateButton = button(*.cls := "btn btn-default")("查询").render

  private val GroupMap = mutable.HashMap[String, Group]()

  private val onLineMap = mutable.HashMap[String, Long]()

  private var onlinePerson = 0

  private var inPerson = 0

  private var outPerson = 0

  private var stayTime = 0l

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

            case msg@ComeIn(mac, time) =>
              println(s"comeIn.i got a msg:$msg")
              onLineMap.put(mac, time)
              inPerson = inPerson + 1
              onlinePerson = onlinePerson + 1
              areaDiv.innerHTML = ""
              areaDiv.appendChild(
                div(
                  span(s"区域内人数:$onlinePerson"),
                  span(s"进区域人数:$inPerson"),
                  span(s"出区域人数:$outPerson"),
                  span(s"驻留时长:$stayTime")
                ).render
              )

            case msg@GetOut(macs) =>
              println(s"i got a msg:$msg")
              macs.map{ m => onLineMap.remove(m)}
              outPerson = outPerson + macs.length
              onlinePerson = onlinePerson - macs.length
              stayTime = System.currentTimeMillis() - onLineMap.toList.sortBy(_._2).head._2
              areaDiv.innerHTML = ""
              areaDiv.appendChild(
                div(
                  span(s"区域内人数:$onlinePerson"),
                  span(s"进区域人数:$inPerson"),
                  span(s"出区域人数:$outPerson"),
                  span(s"驻留时长:$stayTime")
                ).render
              )

            case msg@NowInfo(onlineSum, inSum, outSum) =>
              println(s"i got a msg:$msg")
              onLineMap ++= onlineSum.map(a => (a._1, a._2))
              onlinePerson = onlineSum.length
              inPerson = inSum
              outPerson = outSum
              stayTime = System.currentTimeMillis() - onlineSum.sortBy(_._2).head._2
              areaDiv.innerHTML = ""
              areaDiv.appendChild(
                div(
                  span(s"区域内人数:$onlinePerson"),
                  span(s"进区域人数:$inPerson"),
                  span(s"出区域人数:$outPerson"),
                  span(s"驻留时长:$stayTime")
                ).render
              )
              val newDiv = div(
                table(*.cls := "table")(
                  thead(
                    tr(
                      th(*.textAlign.center)("在线mac")
                    )
                  ),
                  tbody(*.textAlign.center)(
                    onlineSum.map( m => makeRow(m._1))
                  )
                )
              ).render
              onLineDiv.innerHTML = ""
              onLineDiv.appendChild(newDiv)

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

  def makeRow(mac: String) = {
    tr(
      td(mac)
    )
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
        areaDiv,
        onLineDiv
      )
    ).render
  }

}
