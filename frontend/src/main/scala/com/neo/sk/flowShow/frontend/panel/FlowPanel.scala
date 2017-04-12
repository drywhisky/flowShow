package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.Panel
import com.neo.sk.flowShow.ptcl.{Heartbeat, WebSocketMsg, ComeIn, GetOut}
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
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dry on 2017/4/10.
  */
object FlowPanel extends Panel{

  private val realTimeChartIn = canvas(*.width := "500").render
  private val realTimeChartOut = canvas(*.width := "500").render

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

//  private def drawChart(area: Canvas,data:List[TimeDot],`type`:String,label:String,rank:Int) = {
//    import com.neo.sk.feeler3.frontend.business.facede.chartjs2._
//    import js.JSConverters._
//
//    println("drawChart")
//    val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
//    val (xs, ys) = data.sortBy(_.timestamp).map(d => (MyUtil.DateFormatter(new Date(d.timestamp),`type`), d.value.toDouble)).unzip
//    val dataSet = new LineDataSet(data = ys.toJSArray, "label")
//    val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
//    rank match{
//      case 1 =>
//        realTimeInstance.foreach(_.destroy())
//        realTimeInstance = Some( new Chart(ctx, new ChartConfig("line", chartData,null)))
//      case 2 =>
//        realTimeOutInstance.foreach(_.destroy())
//        realTimeOutInstance = Some( new Chart(ctx, new ChartConfig("line", chartData,null)))
//    }
//  }
//
//
//  private def updateRealTimeDetail(id: Int) = {
//    id match {
//      case 1 =>
//        drawChart(realTimeChartIn,realTimeDetailData,"hh:mm","顾客 进店",1)
//
//      case 2 =>
//        drawChart(realTimeChartOut,realTimeDetailData,"hh:mm","顾客 穿行",2)
//
//    }
//  }

  override def locationHash = ""

  override protected def build(): Div = {
    div(*.cls := "row alt")(
      h1(*.cls:="t1")("实时客流图表"),
      div(
        div(*.cls:="in")("进店"),
        realTimeChartIn
      ),
      div(
        div(*.cls:="out")("穿行"),
        realTimeChartOut
      )
    ).render
  }

}
