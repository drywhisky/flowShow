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
  * @note JavaScript name: <code>plotOptions-columnrange-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsColumnrangePoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsColumnrangePointEvents]] = js.undefined
}

object PlotOptionsColumnrangePoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsColumnrangePointEvents]] = js.undefined): PlotOptionsColumnrangePoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsColumnrangePointEvents]] = events
    new PlotOptionsColumnrangePoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsColumnrangePointEvents]] = eventsOuter
    }
  }
}
