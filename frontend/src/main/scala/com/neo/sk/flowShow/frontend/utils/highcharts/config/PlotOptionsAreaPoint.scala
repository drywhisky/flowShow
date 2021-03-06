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
  * @note JavaScript name: <code>plotOptions-area-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsAreaPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsAreaPointEvents]] = js.undefined
}

object PlotOptionsAreaPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsAreaPointEvents]] = js.undefined): PlotOptionsAreaPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsAreaPointEvents]] = events
    new PlotOptionsAreaPoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsAreaPointEvents]] = eventsOuter
    }
  }
}
