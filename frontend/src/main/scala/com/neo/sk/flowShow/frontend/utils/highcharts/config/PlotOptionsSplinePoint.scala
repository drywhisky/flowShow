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
  * @note JavaScript name: <code>plotOptions-spline-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsSplinePoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsSplinePointEvents]] = js.undefined
}

object PlotOptionsSplinePoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsSplinePointEvents]] = js.undefined): PlotOptionsSplinePoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsSplinePointEvents]] = events
    new PlotOptionsSplinePoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsSplinePointEvents]] = eventsOuter
    }
  }
}
