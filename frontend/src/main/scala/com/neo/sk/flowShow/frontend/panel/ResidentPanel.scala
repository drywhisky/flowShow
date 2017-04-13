package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.html.{Canvas, Div}
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.ptcl._
import org.scalajs.dom.raw.CanvasRenderingContext2D
import scala.scalajs.js
import scala.collection.mutable
import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.facede.chartjs2._
import io.circe.generic.auto._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dry on 2017/4/11.
  */
object ResidentPanel extends Panel{

  private val residentChart = canvas(*.width := "500").render
  private val residentMap = mutable.HashMap[Int, ResidentInfo]()
  private var residentInstance : Option[Chart] = None

  private val timeMap = Map(
    0 -> "1min以下",
    1 -> "1-10min",
    2 ->"10-30min",
    3 -> "30min-1h",
    4 -> "1h-2h",
    5 -> "2h以上"
  )

  override def locationHash = ""

  def drawResidentChart(area: Canvas, dataOpt: Option[ResidentInfo], `type`:String) = {
    dataOpt match {
      case Some(r) =>
        import js.JSConverters._
        val data = r.data
        val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        val (xs, ys) = data.sortBy(_.duration).map(d => (timeMap(d.duration), d.num.toDouble)).unzip
        val dataSet = new LineDataSet(data = ys.toJSArray, label = `type`, fill = true)
        val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
        residentInstance.foreach(_.destroy())
        residentInstance = Some(new Chart(ctx, new ChartConfig("line", chartData,null)))

      case None =>
        //doNothing
    }
  }

  private def initData() = {
    Http.getAndParse[ResidentInfoRsp](Routes.residentUrl).map{ rsp =>
      if(rsp.errCode == 0){
        rsp.data match {
          case Some(r) =>
            residentMap ++= r.map(o => (o.groupId.toInt, o))
            drawResidentChart(residentChart, residentMap.get(11), "驻留时长")

          case None =>

        }

      }else{
        JsFunc.alert(s"${rsp.msg}")
      }
    }
  }

  override protected def build() : Div = {
    initData()
    div(*.cls := "row")(
      div(*.cls := "col-md-12", *.textAlign := "center")(
        h1(
          span(*.cls := "artpip-highlight", *.color := "#13C5E4")("昨日驻留时长")
        )
      ),
      div(*.cls := "half-chart-right")(
        residentChart
      )
    ).render
  }
}
