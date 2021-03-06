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
  * @note JavaScript name: <code>plotOptions-series-marker-states</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsSeriesMarkerStates extends js.Object {

  val hover: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesHover]] = js.undefined

  /**
    * The appearance of the point marker when selected. In order to allow a point to be 
    * 		selected, set the <code>series.allowPointSelect</code> option to true.
    */
  val select: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesSelect]] = js.undefined
}

object PlotOptionsSeriesMarkerStates {
  /**
    * @param select The appearance of the point marker when selected. In order to allow a point to be . 		selected, set the <code>series.allowPointSelect</code> option to true.
    */
  def apply(hover: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesHover]] = js.undefined, select: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesSelect]] = js.undefined): PlotOptionsSeriesMarkerStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesHover]] = hover
    val selectOuter: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesSelect]] = select
    new PlotOptionsSeriesMarkerStates {
      override val hover: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesHover]] = hoverOuter
      override val select: js.UndefOr[CleanJsObject[PlotOptionsSeriesMarkerStatesSelect]] = selectOuter
    }
  }
}
