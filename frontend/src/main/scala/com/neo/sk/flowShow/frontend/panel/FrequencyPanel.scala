package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.facede.chartjs2.{BarDataSet, Chart, ChartConfig, ChartData}
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.html.{Canvas, Div}
import io.circe.generic.auto._
import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.ptcl._
import org.scalajs.dom.raw.CanvasRenderingContext2D
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.scalajs.js

/**
  * Created by dry on 2017/4/11.
  */
object FrequencyPanel extends Panel{

  private val frequencyChart = canvas(*.width := "500").render
  private val frequencyMap = mutable.HashMap[Int, FrequencyInfo]()
  private var frequencyInstance : Option[Chart] = None

  override def locationHash = ""


  private def drawFrequencyChart(area: Canvas, dataOpt: Option[FrequencyInfo], `type`: String) = {
    dataOpt match {
      case Some(r) =>
        import js.JSConverters._
        val data = r.visitFrequency
        println("drawBarChart")
        val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        val (xs, ys) = data.sortBy(_.frequency).map(d => (d.frequency, d.count)).unzip
        val dataSet = new BarDataSet(data = ys.toJSArray, label=`type`)
        val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
        frequencyInstance.foreach(_.destroy())
        frequencyInstance = Some(new Chart(ctx, new ChartConfig("horizonBar", chartData, null)))


      case None =>
        //do nothing
    }
  }

  private def initData() = {
    Http.getAndParse[FrequencyInfoRsp](Routes.frequencyUrl).map{ rsp =>
      if(rsp.errCode == 0) {
        rsp.data match {
          case Some(r) =>
            frequencyMap ++= r.map(o => (o.groupId.toInt, o))
            drawFrequencyChart(frequencyChart, frequencyMap.get(11), "到访频次")

          case None =>
            JsFunc.alert(s"rsp.data is None.")
        }

      }else{
        JsFunc.alert(s"${rsp.msg}")
      }

    }
  }

  override protected def build() : Div = {
    initData()
    div(*.cls := "row")(
      frequencyChart
    ).render
  }

}
