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
  * @note JavaScript name: <code>series&lt;treemap&gt;-point</code>
  */
@js.annotation.ScalaJSDefined
class SeriesTreemapPoint extends js.Object {

  /**
    * Events for each single point
    */
  val events: js.UndefOr[CleanJsObject[SeriesTreemapPointEvents]] = js.undefined
}

object SeriesTreemapPoint {
  /**
    * @param events Events for each single point
    */
  def apply(events: js.UndefOr[CleanJsObject[SeriesTreemapPointEvents]] = js.undefined): SeriesTreemapPoint = {
    val eventsOuter: js.UndefOr[CleanJsObject[SeriesTreemapPointEvents]] = events
    new SeriesTreemapPoint {
      override val events: js.UndefOr[CleanJsObject[SeriesTreemapPointEvents]] = eventsOuter
    }
  }
}
