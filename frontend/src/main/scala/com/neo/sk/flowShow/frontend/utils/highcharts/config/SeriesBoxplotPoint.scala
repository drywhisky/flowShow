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
  * @note JavaScript name: <code>series&lt;boxplot&gt;-point</code>
  */
@js.annotation.ScalaJSDefined
class SeriesBoxplotPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[SeriesBoxplotPointEvents]] = js.undefined
}

object SeriesBoxplotPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[SeriesBoxplotPointEvents]] = js.undefined): SeriesBoxplotPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[SeriesBoxplotPointEvents]] = events
    new SeriesBoxplotPoint {
      override val events: js.UndefOr[CleanJsObject[SeriesBoxplotPointEvents]] = eventsOuter
    }
  }
}
