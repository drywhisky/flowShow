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
  * @note JavaScript name: <code>plotOptions-scatter-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsScatterPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsScatterPointEvents]] = js.undefined
}

object PlotOptionsScatterPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsScatterPointEvents]] = js.undefined): PlotOptionsScatterPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsScatterPointEvents]] = events
    new PlotOptionsScatterPoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsScatterPointEvents]] = eventsOuter
    }
  }
}
