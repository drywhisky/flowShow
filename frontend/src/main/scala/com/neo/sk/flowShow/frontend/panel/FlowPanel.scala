package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.Panel
import com.neo.sk.flowShow.ptcl.{ComeIn, GetOut, Heartbeat, WebSocketMsg}
import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom.{Event, window}
import io.circe.generic.auto._

import scalatags.JsDom.short._
import org.scalajs.dom.html.{Canvas, Div}
import org.scalajs.dom.raw.{CanvasRenderingContext2D, Document, MessageEvent, WebSocket}

import scala.scalajs.js
import scala.scalajs.js.Date
import com.neo.sk.flowShow.frontend.utils.MyUtil

import js.JSConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.frontend.Routes

import scala.collection.mutable
import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsAliases._
import com.neo.sk.flowShow.frontend.utils.highcharts.config._
import org.scalajs.jquery.{JQueryEventObject, jQuery}

/**
  * Created by dry on 2017/4/10.
  */
object FlowPanel extends Panel{

  private val realTimeChart = div().render
  private val realTimeMap = mutable.HashMap[Int, RealTimeInfo]()

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
            jQuery("div[data-highcharts-chart]").each { (_: Int, e: dom.Element) ⇒
              jQuery(e).highcharts().foreach(_.series.apply(0).addPoint(options = SeriesSplineData(x = new Date().getTime(), y = Math.random()), redraw = true, shift = true)).asInstanceOf[js.Any]
            }

          case msg@ComeIn(_) =>
            println(s"comeIn.i got a msg:$msg")

          case msg@GetOut(_) =>
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

  def getWebsocketUrl(document: Document): String = {
    val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://${dom.document.location.host}/flowShow/ws/home"
  }

  def parse[T](s: String)(implicit decoder: Decoder[T]): Either[Error, T] = {
    import io.circe.parser._
    decode[T](s)
  }


//  private def initData() = {
//    Http.getAndParse[RealTimeInfoRsp](Routes.realTimeUrl).map { rsp =>
//      if (rsp.errCode == 0) {
//        rsp.data match {
//          case Some(r) =>
//            realTimeMap ++= r.map(o => (o.groupId.toInt, o))
//            drawRealTimeChart(realTimeChart, realTimeMap.get(11))
//
//          case None =>
//            JsFunc.alert(s"rsp.data is None")
//        }
//      }else{
//        JsFunc.alert(s"${rsp.msg}")
//      }
//    }
//  }

  val test = new HighchartsConfig {

    // Chart config
    override val chart: Cfg[Chart] = Chart(`type` = "spline", marginRight = 10)

    // Chart title
    override val title: Cfg[Title] = Title(text = "动态模拟实时数据")

    // X Axis settings
    override val xAxis: CfgArray[XAxis] = js.Array(XAxis(`type` = "datetime", tickPixelInterval = 150))

    // Y Axis settings
    override val yAxis: CfgArray[YAxis] = js.Array(YAxis(title = YAxisTitle(text = "值"), plotLines = js.Array(YAxisPlotLines(value = 0.0, width = 1.0, color = "#808080"))))

    // Series
    override val series: SeriesCfg = js.Array[AnySeries](
      SeriesSpline(name = "随机",
        data = getTime().toJSArray
      )
    )
  }

  def getTime() = {
    val time = new Date().getTime()
    val a = (-19 to 0).map{ i =>  SeriesSplineData(x = time + i * 1000, y = Math.random())}.toList
    a
  }
  private def renderChart(chartConfig: CleanJsObject[js.Object], container:Div) = {
    dom.console.log(chartConfig)
    val newContainer = div().render
    jQuery(newContainer).highcharts(chartConfig)
    container.innerHTML = ""
    container.appendChild(newContainer)
  }

  override def locationHash = ""

  override protected def build(): Div = {
//    initData()
    renderChart(test, realTimeChart)
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-12", *.textAlign := "center")(
          h1(
            span(*.cls := "artpip-highlight", *.color := "#13C5E4")("实时客流统计")
          )
        )
      ),
      div(*.cls := "row", *.width := "50%", *.height := "50%")(
        realTimeChart
      )
    ).render
  }

}
