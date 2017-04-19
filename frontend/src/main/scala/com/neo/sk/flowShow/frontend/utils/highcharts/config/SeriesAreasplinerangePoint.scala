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
  * @note JavaScript name: <code>series&lt;areasplinerange&gt;-point</code>
  */
@js.annotation.ScalaJSDefined
class SeriesAreasplinerangePoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[SeriesAreasplinerangePointEvents]] = js.undefined
}

object SeriesAreasplinerangePoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[SeriesAreasplinerangePointEvents]] = js.undefined): SeriesAreasplinerangePoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[SeriesAreasplinerangePointEvents]] = events
    new SeriesAreasplinerangePoint {
      override val events: js.UndefOr[CleanJsObject[SeriesAreasplinerangePointEvents]] = eventsOuter
    }
  }
}
