//package com.neo.sk.flowShow.frontend.panel
//
//import com.neo.sk.flowShow.frontend.utils.Panel
//import com.neo.sk.flowShow.ptcl.{ComeIn, GetOut, Heartbeat, WebSocketMsg}
//import io.circe.{Decoder, Error}
//import org.scalajs.dom
//import org.scalajs.dom.{Event, MouseEvent, window}
//import io.circe.generic.auto._
//
//import scalatags.JsDom.short._
//import org.scalajs.dom.html.Div
//import org.scalajs.dom.raw.{Document, MessageEvent, WebSocket}
//
//import scala.scalajs.js
//import scala.scalajs.js.Date
//
//import js.JSConverters._
//import scala.concurrent.ExecutionContext.Implicits.global
//import com.neo.sk.flowShow.ptcl._
//import com.neo.sk.flowShow.frontend.utils.Http
//import com.neo.sk.flowShow.frontend.Routes
//
//import scala.collection.mutable
//import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
//import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._
//import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsAliases._
//import com.neo.sk.flowShow.frontend.utils.highcharts.config._
//import org.scalajs.jquery.{JQueryEventObject, jQuery}
//
//
///**
//  * Created by dry on 2017/4/10.
//  */
//object FlowPanel extends Panel{
//
//  private val realTimeChart = div().render
//
//  private val rangeIndex = select(*.width := "150px", *.color := "black", *.height := "30px", *.marginRight := "10px").render
//
//  private val searchByDateButton = button(*.cls := "btn btn-default")("查询").render
//
//  private val searchByIdIncome =
//    div(
//      form(*.cls := "form-inline", *.marginLeft := "8%")(
//        div(*.cls := "form-group")(
//          rangeIndex
//        ),
//        searchByDateButton
//      )
//    ).render
//
//  searchByDateButton.onclick = { e: MouseEvent =>
//    e.preventDefault()
//    val mac = rangeIndex.value
//    openWs(mac)
//  }
//
//  def openWs(mac: String) = {
//    val ws = new WebSocket(getWebsocketUrl(dom.document, mac))
//
//    ws.onopen = { (e: Event) =>
//      println(s"ws.onopen...${e.timeStamp}")
//
//      val drawChart = new HighchartsConfig {
//
//        // Chart config
//        override val chart: Cfg[Chart] = Chart(`type` = "spline")
//
//        // Chart title
//        override val title: Cfg[Title] = Title(text = "动态实时数据")
//
//        // X Axis settings
//        override val xAxis: CfgArray[XAxis] = js.Array(XAxis(`type` = "datetime", tickPixelInterval = 150))
//
//        // Y Axis settings
//        override val yAxis: CfgArray[YAxis] = js.Array(YAxis(title = YAxisTitle(text = "值"), plotLines = js.Array(YAxisPlotLines(value = 0.0, width = 1.0, color = "#808080"))))
//
//        // Series
//        override val series: SeriesCfg = js.Array[AnySeries](
//          SeriesSpline(name = "随机",
//            data = getRadom().toJSArray
//          )
//        )
//      }
//
//      realTimeChart.innerHTML = ""
//
//      renderChart(drawChart, realTimeChart)
//
//    }
//
//    ws.onmessage = { (e: MessageEvent) =>
//
//      val wsMsg = parse[WebSocketMsg](e.data.toString)
//
//      wsMsg match {
//        case Right(messages) =>
//
//          messages match {
//            case Heartbeat(id) =>
//              println(s"i got a Heartbeat")
//              jQuery("div[data-highcharts-chart]").each { (_: Int, e: dom.Element) ⇒
//                jQuery(e).highcharts().foreach(_.series.apply(0).addPoint(options = SeriesSplineData(x = new Date().getTime()+ (8 * 3600 * 1000), y = Math.random(), color = "red"), redraw = true, shift = true)).asInstanceOf[js.Any]
//              }
//
//            case msg@ComeIn(num, time) =>
//              println(s"comeIn.i got a msg:$msg")
//              jQuery("div[data-highcharts-chart]").each { (_: Int, e: dom.Element) ⇒
//                jQuery(e).highcharts().foreach(_.series.apply(0).addPoint(options = SeriesSplineData(x = new Date().getTime()+ (8 * 3600 * 1000), y = 1, color = "yellow"), redraw = true, shift = true)).asInstanceOf[js.Any]
//              }
//
//            case msg@GetOut(num) =>
//              println(s"i got a msg:$msg")
//              jQuery("div[data-highcharts-chart]").each { (_: Int, e: dom.Element) ⇒
//                jQuery(e).highcharts().foreach(_.series.apply(0).addPoint(options = SeriesSplineData(x = new Date().getTime()+ (8 * 3600 * 1000), y = 0 - num.length, color = "yellow"), redraw = true, shift = true)).asInstanceOf[js.Any]
//              }
//
//            case x =>
//              println(s"i got a msg:$x")
//          }
//
//        case Left(e) =>
//          println(s"wsMsg match fail...$e")
//
//      }
//
//    }
//
//    ws.onclose = {
//      (e: Event) =>
//        window.alert("ws 断开")
//    }
//
//  }
//
//  def getWebsocketUrl(document: Document, mac: String): String = {
//    val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"
//
//    s"$wsProtocol://${dom.document.location.host}/flowShow/ws/home?subId=$mac"
//  }
//
//  def parse[T](s: String)(implicit decoder: Decoder[T]): Either[Error, T] = {
//    import io.circe.parser._
//    decode[T](s)
//  }
//
//  private def getRadom() = {
//    val time = new Date().getTime() + (8 * 3600 * 1000)
//    val a = (-19 to 0).map{ i =>  SeriesSplineData(x = time + i * 1000, y = Math.random())}.toList
//    a
//  }
//
//  private def renderChart(chartConfig: CleanJsObject[js.Object], container:Div) = {
//    dom.console.log(chartConfig)
//    val newContainer = div().render
//    jQuery(newContainer).highcharts(chartConfig)
//    container.innerHTML = ""
//    container.appendChild(newContainer)
//  }
//
//  def makeBoxsSelects(list: List[Box]) = {
//
//    def makeBoxSelects(box: Box) = {
//      rangeIndex.appendChild(option(s"${box.mac}").render)
//    }
//
//    list.map(data => makeBoxSelects(data))
//
//  }
//
//  def getBoxList() = {
//    Http.getAndParse[BoxsRsp](Routes.getAllBoxs).map { rsp =>
//      if (rsp.errCode == 0) {
//        makeBoxsSelects(rsp.data)
//      }
//    }
//  }
//
//  override def locationHash = ""
//
//  override protected def build(): Div = {
//    getBoxList()
//    div(
//      div(*.cls := "row")(
//        div(*.cls := "col-md-12", *.textAlign := "center")(
//          h1(
//            span(*.cls := "artpip-highlight", *.color := "#13C5E4")("单盒实时客流呈现")
//          )
//        )
//      ),
//      searchByIdIncome,
//      div(*.cls := "row", *.width := "70%", *.height := "50%", *.marginLeft := "20%", *.marginTop := "3%")(
//        realTimeChart
//      )
//    ).render
//  }
//
//}
