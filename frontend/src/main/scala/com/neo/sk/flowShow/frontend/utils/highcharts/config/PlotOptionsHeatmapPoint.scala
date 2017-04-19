/**
  * Automatically generated file. Please do not edit.
  * @author Highcharts Config Generator by Karasiq
  * @see [[http://api.highcharts.com/highcharts]]
  */
package com.neo.sk.flowShow.frontend.utils.highcharts.config

import scalajs.js, js.`|`
import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._

/**
  * @note JavaScript name: <code>plotOptions-heatmap-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsHeatmapPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsHeatmapPointEvents]] = js.undefined
}

object PlotOptionsHeatmapPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsHeatmapPointEvents]] = js.undefined): PlotOptionsHeatmapPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsHeatmapPointEvents]] = events
    new PlotOptionsHeatmapPoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsHeatmapPointEvents]] = eventsOuter
    }
  }
}
