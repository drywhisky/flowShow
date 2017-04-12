package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.facede.chartjs2._
import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.html.{Canvas, Div}
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.ptcl._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._
import org.scalajs.dom.raw.CanvasRenderingContext2D
import scala.scalajs.js
import scalatags.JsDom.short._
import io.circe.generic.auto._

/**
  * Created by dry on 2017/4/11.
  */
object BrandPanel extends Panel{

  private val brandChart = canvas(*.width := "500").render
  private val brandsMap = mutable.HashMap[Int, BrandsInfo]()
  private var brandInstance: Option[Chart] = None

  private def initData() = {
    Http.getAndParse[BrandsInfoRsp](Routes.brandUrl).map { rsp =>
      if (rsp.errCode == 0) {
        rsp.data match {
          case Some(r) =>
            brandsMap ++= r.map(o => (o.groupId.toInt, o))
            drawBrandChart(brandChart, brandsMap.get(11), "MM-DD")

          case None =>
            JsFunc.alert(s"rsp.data is None")
        }
      }else{
        JsFunc.alert(s"${rsp.msg}")
      }
    }
  }

  private def drawBrandChart(area: Canvas,dataOpt:Option[BrandsInfo],`type`:String) = {
    dataOpt match {
      case Some(parent) =>
        import js.JSConverters._
        val data = parent.data
        println("drawpieChart")
        val ctx = area.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
        val tooltip = new tooltips(titleFontSize = 20, bodyFontSize = 20)
        val option = new Options(tooltips = tooltip)
        val xs = data.map(d => d.name)
        val ys = data.map(d => d.num)
        //val (xs, ys) = (Array[String]("进入数","出门数"), Array[Double](data(1).custIn.toDouble,data(1).custOut.toDouble))).unzip
        val dataSet = new PieDataSet(data = ys.toJSArray)
        val chartData = new ChartData(xs.toJSArray, js.Array(dataSet))
        brandInstance.foreach(_.destroy())
        brandInstance = Some(new Chart(ctx, new ChartConfig("doughnut", chartData, option)))

      case None =>
        //doNothing
    }
  }


  override def locationHash = ""

  override protected def build() : Div = {
    initData()
    div(*.cls := "row alt")(
      brandChart
    ).render
  }


}
