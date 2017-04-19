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
  * @note JavaScript name: <code>plotOptions-pyramid-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsPyramidPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsPyramidPointEvents]] = js.undefined
}

object PlotOptionsPyramidPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsPyramidPointEvents]] = js.undefined): PlotOptionsPyramidPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsPyramidPointEvents]] = events
    new PlotOptionsPyramidPoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsPyramidPointEvents]] = eventsOuter
    }
  }
}
