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
  * @note JavaScript name: <code>plotOptions-areasplinerange-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsAreasplinerangePoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsAreasplinerangePointEvents]] = js.undefined
}

object PlotOptionsAreasplinerangePoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsAreasplinerangePointEvents]] = js.undefined): PlotOptionsAreasplinerangePoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsAreasplinerangePointEvents]] = events
    new PlotOptionsAreasplinerangePoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsAreasplinerangePointEvents]] = eventsOuter
    }
  }
}
