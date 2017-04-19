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
  * @note JavaScript name: <code>plotOptions-areaspline-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsAreasplinePoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsAreasplinePointEvents]] = js.undefined
}

object PlotOptionsAreasplinePoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsAreasplinePointEvents]] = js.undefined): PlotOptionsAreasplinePoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsAreasplinePointEvents]] = events
    new PlotOptionsAreasplinePoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsAreasplinePointEvents]] = eventsOuter
    }
  }
}
