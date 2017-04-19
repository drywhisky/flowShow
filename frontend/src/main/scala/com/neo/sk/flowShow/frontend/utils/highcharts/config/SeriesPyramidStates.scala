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
  * @note JavaScript name: <code>series&lt;pyramid&gt;-states</code>
  */
@js.annotation.ScalaJSDefined
class SeriesPyramidStates extends js.Object {

  /**
    * Options for the hovered series
    */
  val hover: js.UndefOr[CleanJsObject[SeriesPyramidStatesHover]] = js.undefined
}

object SeriesPyramidStates {
  /**
    * @param hover Options for the hovered series
    */
  def apply(hover: js.UndefOr[CleanJsObject[SeriesPyramidStatesHover]] = js.undefined): SeriesPyramidStates = {
    val hoverOuter: js.UndefOr[CleanJsObject[SeriesPyramidStatesHover]] = hover
    new SeriesPyramidStates {
      override val hover: js.UndefOr[CleanJsObject[SeriesPyramidStatesHover]] = hoverOuter
    }
  }
}
