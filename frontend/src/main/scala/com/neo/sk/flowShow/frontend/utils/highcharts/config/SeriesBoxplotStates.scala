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
  * @note JavaScript name: <code>series&lt;boxplot&gt;-states</code>
  */
@js.annotation.ScalaJSDefined
class SeriesBoxplotStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[SeriesBoxplotStatesHover]] = js.undefined
}

object SeriesBoxplotStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[SeriesBoxplotStatesHover]] = js.undefined): SeriesBoxplotStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[SeriesBoxplotStatesHover]] = hover
    new SeriesBoxplotStates {
      override val hover: js.UndefOr[CleanJsObject[SeriesBoxplotStatesHover]] = hoverOuter
    }
  }
}
