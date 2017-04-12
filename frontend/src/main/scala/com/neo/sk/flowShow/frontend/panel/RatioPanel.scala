package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.html.{Canvas, Div}

import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.ptcl._
import org.scalajs.dom.raw.CanvasRenderingContext2D
import com.neo.sk.flowShow.frontend.facede.chartjs2._
import io.circe.generic.auto._
import scala.collection.mutable
import scala.scalajs.js
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by dry on 2017/4/11.
  */
object RatioPanel extends Panel{

  private val ratioChart = canvas(*.width := "500").render
  private val ratioMap = mutable.HashMap[Int, RatioInfo]()
  private var ratioInstance : Option[Chart] = None

  override def locationHash = ""

  private def drawRatioChart(area: Canvas, dataOpt: Option[RatioInfo], `type`: String) = {
    dataOpt match {
      case Some(r) =>
        import js.JSConverters._
        val data = r.ratio
        println("drawBarChart")
        val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        val (xs, ys) = data.sortBy(_.date).map(d => (d.date, d.oldRatio)).unzip
        val dataSet = new BarDataSet(data = ys.toJSArray, label=`type`)
        val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
        ratioInstance.foreach(_.destroy())
        ratioInstance = Some(new Chart(ctx, new ChartConfig("verticalBar", chartData, null)))

      case None =>
        //do nothing
    }
  }

  private def initData() = {
    Http.getAndParse[RatioInfoRsp](Routes.ratioUrl).map{ rsp =>
      if(rsp.errCode == 0 ){
        rsp.data match {
          case Some(r) =>
            ratioMap ++= r.map(o => (o.groupId.toInt, o))
            drawRatioChart(ratioChart, ratioMap.get(11), "老客户比")

          case None =>
            JsFunc.alert(s"rsp.data is None")
        }
      }else{
        JsFunc.alert(s"${rsp.msg}")
      }
    }
  }

  override protected def build() : Div = {
    initData()
    div(*.cls := "row alt")(
      ratioChart
    ).render
  }

}
