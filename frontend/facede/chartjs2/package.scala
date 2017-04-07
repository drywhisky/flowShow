package com.neo.sk.feeler3.frontend.business.facede

import scala.scalajs.js
import js.annotation.{ScalaJSDefined, _}
import js.JSConverters._
import scala.scalajs.js.|


/**
  * User: Taoz
  * Date: 12/22/2016
  * Time: 7:18 PM
  */
package object chartjs2 {


  import org.scalajs.dom.Event
  import org.scalajs.dom.CanvasRenderingContext2D


/*  @ScalaJSDefined
  class CharDataSet(
    val data: js.Array[Double],
    val label: String = "",
    val fill: Boolean = false,
    val backgroundColor: js.UndefOr[String] | js.Array[String] = js.undefined,
    val borderColor: js.UndefOr[String] | js.Array[String] = js.undefined
  ) extends js.Object*/

  @ScalaJSDefined
  trait DataSet extends js.Object

  @ScalaJSDefined
  class LineDataSet(
    val data: js.Array[Double],
    val label: String = "",
    val fill: Boolean = false,
//    val scaleLineColor : js.UndefOr[String]   ="rgba(151,187,205,0.5)",
//    val fillColor: js.UndefOr[String]   = "rgba(151,187,205,0.5)",
//    val strokeColor : js.UndefOr[String]  = "rgba(151,187,205,1)",
//    val pointColor : js.UndefOr[String]   ="rgba(151,187,205,1)",
//    val pointStrokeColor :js.UndefOr[String]   = "#fff"
    val backgroundColor: js.UndefOr[String] | js.Array[String] = "#3D86FF",
    val borderColor: js.UndefOr[String] | js.Array[String] = "#3D86FF"
  ) extends DataSet

  @ScalaJSDefined
  class BarDataSet(
    val data: js.Array[Int],
    val label: String = "",
    val backgroundColor: js.UndefOr[String] | js.Array[String] = "#3D86FF",
    val borderColor: js.UndefOr[String] | js.Array[String] = "#3D86FF"
  ) extends DataSet

  @ScalaJSDefined
  class DoubleBarData(
                    val data: js.Array[String],
                    val label: String = "",
                    val backgroundColor: js.UndefOr[String] | js.Array[String] = js.undefined,
                    val borderColor: js.UndefOr[String] | js.Array[String] = js.undefined
                  ) extends DataSet

  @ScalaJSDefined
  class PieDataSet(
                    val data: js.Array[Int],
                    val label: String = "",
                    val backgroundColor: js.UndefOr[String] | js.Array[String] = js.Array("#3D86FF", "#4AEC54", "#00CCCC", "#009CBB", "#781BFF", "#9933CC","#BF007D", "#CC3300","#E4001B","#EA5D0F", "#FF8853", "#FFCC21","#FFFF33","#FFFFBB"),
                    val borderColor: js.UndefOr[String] | js.Array[String] = js.Array("#3D86FF", "#4AEC54", "#00CCCC", "#009CBB", "#781BFF","#9933CC", "#BF007D","#CC3300","#E4001B", "#EA5D0F", "#FF8853", "#FFCC21","#FFFF33","#FFFFBB")
                  ) extends DataSet


  /*
    @js.native
    trait ChartDataSet extends js.Object {
      var label: String = js.native
      var fillColor: String = js.native
      var strokeColor: String = js.native
      var pointColor: String = js.native
      var pointStrokeColor: String = js.native
      var pointHighlightFill: String = js.native
      var pointHighlightStroke: String = js.native
      var highlightFill: String = js.native
      var highlightStroke: String = js.native
      var data: js.Array[Double] = js.native
    }

    object ChartDataSet {
      def apply(
        label: js.UndefOr[String] = js.undefined,
        fillColor: js.UndefOr[String] = js.undefined,
        strokeColor: js.UndefOr[String] = js.undefined,
        pointColor: js.UndefOr[String] = js.undefined,
        pointStrokeColor: js.UndefOr[String] = js.undefined,
        pointHighlightFill: js.UndefOr[String] = js.undefined,
        pointHighlightStroke: js.UndefOr[String] = js.undefined,
        highlightFill: js.UndefOr[String] = js.undefined,
        highlightStroke: js.UndefOr[String] = js.undefined,
        data: js.UndefOr[js.Array[Double]] = js.undefined
      ): ChartDataSet = {
        val result = js.Dynamic.literal()
        label.foreach(result.label = _)
        fillColor.foreach(result.fillColor = _)
        strokeColor.foreach(result.strokeColor = _)
        pointColor.foreach(result.pointColor = _)
        pointStrokeColor.foreach(result.pointStrokeColor = _)
        pointHighlightFill.foreach(result.pointHighlightFill = _)
        pointHighlightStroke.foreach(result.pointHighlightStroke = _)
        highlightFill.foreach(result.highlightFill = _)
        highlightStroke.foreach(result.highlightStroke = _)
        data.foreach(result.data = _)
        result.asInstanceOf[ChartDataSet]
      }
    }*/


  @ScalaJSDefined
  class ChartData(
    val labels: js.Array[String],
    val datasets: js.Array[DataSet]
  ) extends js.Object

  /*  @js.native
    trait ChartData extends js.Object {
      var labels: js.Array[String] = js.native
      var datasets: js.Array[ChartDataSet] = js.native
    }

    object ChartData {
      def apply(
        labels: Seq[String] = Seq.empty[String],
        datasets: Seq[ChartDataSet] = Seq.empty[ChartDataSet]
      ): ChartData = {
        js.Dynamic.literal(
          labels = labels.toJSArray,
          datasets = datasets.toJSArray
        ).asInstanceOf[ChartData]
      }
    }*/

/*
  @js.native
  trait ChartOptions extends js.Object {
    var scaleShowGridLines: Boolean = js.native
    var scaleGridLineColor: String = js.native
    var scaleGridLineWidth: Double = js.native
    var legendTemplate: String = js.native
  }

  object ChartOptions {
    def apply(
      scaleShowGridLines: Boolean = false,
      scaleGridLineColor: String = null,
      scaleGridLineWidth: Double = 1.0,
      legendTemplate: String = null
    ): ChartOptions = {
      js.Dynamic.literal(
        scaleShowGridLines = scaleShowGridLines,
        scaleGridLineColor = scaleGridLineColor,
        scaleGridLineWidth = scaleGridLineWidth,
        legendTemplate = legendTemplate
      ).asInstanceOf[ChartOptions]
    }
  }
*/


  @ScalaJSDefined
  class ChartConfig(
    @JSName("type")
    val chartType: String,
    val data: ChartData,
    val options: Options
  ) extends js.Object


  /*  @js.native
    trait ChartProps extends js.Object {
      var `type`: String = js.native
      var data: ChartData = js.native
    }

    object ChartProps {
      def apply(
        `type`: String,
        data: ChartData
      ): ChartProps = {
        js.Dynamic.literal(
          `type` = `type`,
          data = data
        ).asInstanceOf[ChartProps]
      }
    }*/

  @ScalaJSDefined
  class tooltips(
  val titleFontSize: js.UndefOr[Int],
  val bodyFontSize: js.UndefOr[Int]
  ) extends js.Object

  @ScalaJSDefined
  class Options (
    val responsive: Boolean = true,
    val title: js.Any = js.undefined,
    val tooltips: tooltips
  ) extends js.Object


  @JSName("Chart")
  @js.native
  class Chart(context: CanvasRenderingContext2D, props: ChartConfig) extends js.Object{

    def destroy(): Unit = js.native

  }


}
