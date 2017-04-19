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
  * @note JavaScript name: <code>series&lt;line&gt;-states</code>
  */
@js.annotation.ScalaJSDefined
class SeriesLineStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[SeriesLineStatesHover]] = js.undefined
}

object SeriesLineStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[SeriesLineStatesHover]] = js.undefined): SeriesLineStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[SeriesLineStatesHover]] = hover
    new SeriesLineStates {
      override val hover: js.UndefOr[CleanJsObject[SeriesLineStatesHover]] = hoverOuter
    }
  }
}
