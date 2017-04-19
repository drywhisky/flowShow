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
  * @note JavaScript name: <code>chart-options3d-frame-bottom</code>
  */
@js.annotation.ScalaJSDefined
class ChartOptions3dFrameBottom extends js.Object {

  /**
    * The color of the panel.
    * @since 4.0
    */
  val color: js.UndefOr[String | js.Object] = js.undefined

  /**
    * The thickness of the panel.
    * @since 4.0
    */
  val size: js.UndefOr[Double] = js.undefined
}

object ChartOptions3dFrameBottom {
  /**
    * @param color The color of the panel.
    * @param size The thickness of the panel.
    */
  def apply(color: js.UndefOr[String | js.Object] = js.undefined, size: js.UndefOr[Double] = js.undefined): ChartOptions3dFrameBottom = {
    val colorOuter: js.UndefOr[String | js.Object] = color
    val sizeOuter: js.UndefOr[Double] = size
    new ChartOptions3dFrameBottom {
      override val color: js.UndefOr[String | js.Object] = colorOuter
      override val size: js.UndefOr[Double] = sizeOuter
    }
  }
}
