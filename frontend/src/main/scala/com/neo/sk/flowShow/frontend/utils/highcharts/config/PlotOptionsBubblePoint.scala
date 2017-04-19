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
  * @note JavaScript name: <code>plotOptions-bubble-point</code>
  */
@js.annotation.ScalaJSDefined
class PlotOptionsBubblePoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[PlotOptionsBubblePointEvents]] = js.undefined
}

object PlotOptionsBubblePoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[PlotOptionsBubblePointEvents]] = js.undefined): PlotOptionsBubblePoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[PlotOptionsBubblePointEvents]] = events
    new PlotOptionsBubblePoint {
      override val events: js.UndefOr[CleanJsObject[PlotOptionsBubblePointEvents]] = eventsOuter
    }
  }
}
