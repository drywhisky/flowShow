package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsAliases._
import com.neo.sk.flowShow.frontend.utils.highcharts.config._
import com.neo.sk.flowShow.frontend.utils.highcharts.Highcharts
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom
import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
import scala.scalajs.js
import org.scalajs.dom.html.Div
import scalatags.JsDom.short._
import scalajs.js
import org.scalajs.jquery.jQuery

/**
  * Created by dry on 2017/4/19.
  */
object TestPanel extends Panel{

  val test = new HighchartsConfig {
    // Chart config
    override val chart: Cfg[Chart] = Chart(`type` = "bar")

    // Chart title
    override val title: Cfg[Title] = Title(text = "Demo bar chart")

    // X Axis settings
    override val xAxis: CfgArray[XAxis] = js.Array(XAxis(categories = js.Array("Apples", "Bananas", "Oranges")))

    // Y Axis settings
    override val yAxis: CfgArray[YAxis] = js.Array(YAxis(title = YAxisTitle(text = "Fruit eaten")))

    // Series
    override val series: SeriesCfg = js.Array[AnySeries](
      SeriesBar(name = "Jane", data = js.Array[Double](1, 0, 4)),
      SeriesBar(name = "John", data = js.Array[Double](5, 7, 3))
    )
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
