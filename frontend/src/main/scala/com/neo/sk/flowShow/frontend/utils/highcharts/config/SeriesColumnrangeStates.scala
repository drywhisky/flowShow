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
  * @note JavaScript name: <code>series&lt;columnrange&gt;-states</code>
  */
@js.annotation.ScalaJSDefined
class SeriesColumnrangeStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[SeriesColumnrangeStatesHover]] = js.undefined
}

object SeriesColumnrangeStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[SeriesColumnrangeStatesHover]] = js.undefined): SeriesColumnrangeStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[SeriesColumnrangeStatesHover]] = hover
    new SeriesColumnrangeStates {
      override val hover: js.UndefOr[CleanJsObject[SeriesColumnrangeStatesHover]] = hoverOuter
    }
  }
}
