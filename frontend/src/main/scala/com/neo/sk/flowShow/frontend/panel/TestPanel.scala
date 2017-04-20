package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsAliases._
import com.neo.sk.flowShow.frontend.utils.highcharts.config._
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom
import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
import scala.scalajs.js.Date
import scala.scalajs.js.Function
import org.scalajs.dom.html.Div

import scalatags.JsDom.short._
import scalajs.js
import org.scalajs.jquery.jQuery
import js.JSConverters._

import scala.scalajs.js.UndefOr

/**
  * Created by dry on 2017/4/19.
  */
object TestPanel extends Panel{

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

  private val container = div().render

  private def renderChart(chartConfig: CleanJsObject[js.Object], container:Div) = {
    dom.console.log(chartConfig)
    val newContainer = div().render
    jQuery(newContainer).highcharts(chartConfig)
    container.innerHTML = ""
    container.appendChild(newContainer)
  }

  override def locationHash = ""

  override protected def build() : Div = {
    renderChart(test, container)
    div(*.id := "container")(
      container
    ).render
  }

}
