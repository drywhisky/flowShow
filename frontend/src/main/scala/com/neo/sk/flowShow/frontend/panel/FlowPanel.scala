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
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.frontend.Routes
import scala.collection.mutable
import com.neo.sk.flowShow.frontend.facede.chartjs2._

/**
  * Created by dry on 2017/4/10.
  */
object FlowPanel extends Panel{

  private val realTimeChart = canvas(*.width := "500").render
  private val realTimeMap = mutable.HashMap[Int, RealTimeInfo]()
  private var realTimeInstance : Option[Chart] = None

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

  def drawRealTimeChart(area: Canvas, dataOpt: Option[RealTimeInfo]) = {
    dataOpt match {
      case Some(r) =>
        import js.JSConverters._
        val data = r.flow
        println("drawChart")
        val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        val (xs, ys) = data.sortBy(_.timestamp).map(d => (MyUtil.DateFormatter(new Date(d.timestamp), "hh:mm"), d.count.toDouble)).unzip
        val dataSet = new LineDataSet(data = ys.toJSArray, "label")
        val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
        realTimeInstance.foreach(_.destroy())
        realTimeInstance = Some( new Chart(ctx, new ChartConfig("line", chartData, null)))

      case None =>
        //do noting
    }
  }

  private def initData() = {
    Http.getAndParse[RealTimeInfoRsp](Routes.realTimeUrl).map { rsp =>
      if (rsp.errCode == 0) {
        rsp.data match {
          case Some(r) =>
            realTimeMap ++= r.map(o => (o.groupId.toInt, o))
            drawRealTimeChart(realTimeChart, realTimeMap.get(11))

          case None =>
            JsFunc.alert(s"rsp.data is None")
        }
      }else{
        JsFunc.alert(s"${rsp.msg}")
      }
    }
  }


  override def locationHash = ""

  override protected def build(): Div = {
    initData()
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
