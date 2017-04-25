package com.neo.sk.flowShow.frontend.panel


import com.neo.sk.flowShow.frontend.utils.Panel
import com.neo.sk.flowShow.ptcl.{ComeIn, GetOut, Heartbeat, WebSocketMsg}
import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom.{Event, MouseEvent, window}
import io.circe.generic.auto._

import scalatags.JsDom.short._
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{Document, MessageEvent, WebSocket}

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.flowShow.frontend.utils.Http
import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._
import com.neo.sk.flowShow.frontend.utils.highcharts.config._
import org.scalajs.jquery.jQuery

import scala.collection.mutable

/**
  * Created by whisky on 17/4/24.
  */
object AreaPanel extends Panel {

  private val areaDiv = div().render

  private val range = div(*.cls := "layui-input-block")(div()).render

  private var rangeIndex = select().render

  private val searchByDateButton = button(*.cls := "layui-btn")("查询").render

  private val GroupMap = mutable.HashMap[String, Long]()

  private val searchByIdIncome =
    div(
      form(*.cls := "layui-form")(
        div(*.cls := "layui-form-item")(
          range
        ),
        div(*.cls := "layui-form-item")(
          div(*.cls := "layui-input-block")(
            searchByDateButton
          )
        )
      )
    ).render

  searchByDateButton.onclick = {
    e: MouseEvent =>
      e.preventDefault()
      val roomName = rangeIndex.value
      val a = GroupMap.getOrElse(roomName, 0)
      openWs()
  }

  def openWs() = {

    def getWebsocketUrl(document: Document): String = {
      val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"

      s"$wsProtocol://${dom.document.location.host}/flowShow/ws/home"
    }

    def parse[T](s: String)(implicit decoder: Decoder[T]): Either[Error, T] = {
      import io.circe.parser._
      decode[T](s)
    }
  }

  def makeGroupsSelects(list: List[Group]) = {

    def makeGroupSelects(group: Group) = {
      option(s"${group.name}")
    }

    rangeIndex = select(
      list.map(data => makeGroupSelects(data))
    ).render

    rangeIndex
  }

  def getGroupList() = {
    Http.getAndParse[GroupsRsp](Routes.getGroups).map { rsp =>
      if (rsp.errCode == 0) {
        GroupMap ++= rsp.data.map(g=> (g.name, g.id))
        makeGroupsSelects(rsp.data)
      } else {
        div(h5("getGroupList error")).render
      }
    }.foreach(elm => range.replaceChild(elm, range.firstChild))
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
      div(*.cls := "row", *.width := "50%", *.height := "50%")(
        areaDiv
      )
    ).render
  }

}
