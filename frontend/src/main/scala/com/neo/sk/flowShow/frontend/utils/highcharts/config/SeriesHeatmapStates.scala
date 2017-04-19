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
  * @note JavaScript name: <code>series&lt;heatmap&gt;-states</code>
  */
@js.annotation.ScalaJSDefined
class SeriesHeatmapStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[SeriesHeatmapStatesHover]] = js.undefined
}

object SeriesHeatmapStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[SeriesHeatmapStatesHover]] = js.undefined): SeriesHeatmapStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[SeriesHeatmapStatesHover]] = hover
    new SeriesHeatmapStates {
      override val hover: js.UndefOr[CleanJsObject[SeriesHeatmapStatesHover]] = hoverOuter
    }
  }
}
