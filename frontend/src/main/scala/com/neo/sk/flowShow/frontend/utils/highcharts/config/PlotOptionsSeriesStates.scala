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
  * @note JavaScript name: <code>plotOptions-series-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsSeriesStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[PlotOptionsSeriesStatesHover]] = js.undefined
}

object PlotOptionsSeriesStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsSeriesStatesHover]] = js.undefined): PlotOptionsSeriesStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsSeriesStatesHover]] = hover
    new PlotOptionsSeriesStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsSeriesStatesHover]] = hoverOuter
    }
  }
}
