package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.{Http, Panel, Shortcut}
import com.neo.sk.flowShow.ptcl.{ComeIn, GetOut, Heartbeat, WebSocketMsg}
import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom.{Event, MouseEvent, window}
import io.circe.generic.auto._

import scalatags.JsDom.short._
import org.scalajs.dom.html.{Div, IFrame}
import org.scalajs.dom.raw.{Document, MessageEvent, WebSocket}

import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.highcharts.CleanJsObject
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsUtils._
import com.neo.sk.flowShow.frontend.utils.highcharts.HighchartsAliases._
import com.neo.sk.flowShow.frontend.utils.highcharts.config._
import org.scalajs.jquery.{JQueryEventObject, jQuery}

import scala.collection.mutable
import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.Date

/**
  * Created by whisky on 17/4/24.
  */

object AreaPanel extends Panel {

  private val areaDiv = div(*.cls := "row info-blips")().render

  private val onLineDiv = div(*.cls := "col-md-2 col-md-offset-1", *.backgroundColor := "#282B3F")().render

  private val rangeIndex = select(*.width := "150px", *.color := "black", *.height := "30px", *.marginRight := "10px").render

  private val searchByDateButton = button(*.cls := "btn btn-default")("查询").render

  private val realTimeChart = div(*.cls := "row")().render

  private val oldPeopleChart = div(*.cls := "row")().render

  private val GroupMap = mutable.HashMap[String, Group]()

  private val onLineMap = mutable.HashMap[String, Long]()

  private var onlinePerson = 0

  private var inPerson = 0

  private var outPerson = 0

  private var oldPerson = 0

  private var stayTime = (0l, 0l)  //(historyMax, NowMax)

  private val searchByIdIncome =
    div(
      form(*.cls := "form-inline", *.marginLeft := "8%")(
        div(*.cls := "form-group")(
          rangeIndex
        ),
        searchByDateButton
      )
    ).render

  searchByDateButton.onclick = {
    e: MouseEvent =>
      e.preventDefault()
      val roomName = rangeIndex.value
      val group = GroupMap.get(roomName).head
      openWs(group.id.toString)

  }

  private def drawOldChart(oldPerson: Int, allPerson: Int) = {

    val drawChart = new HighchartsConfig {

      // Chart config
      override val chart: Cfg[Chart] = Chart(`type` = "pie", options3d = ChartOptions3d(alpha = 45, beta = 0, enabled = true))

      // Chart title
      override val title: Cfg[Title] = Title(text = "区域新老客比")

      override val tooltip: Cfg[Tooltip] = Tooltip(pointFormat = "{series.name}: <b>{point.percentage:.1f}%</b>")

      override val plotOptions: Cfg[PlotOptions] = PlotOptions(pie = PlotOptionsPie(
        allowPointSelect = true,
        cursor = "pointer",
        depth = 35,
        dataLabels = PlotOptionsPieDataLabels(enabled = true, format = "{point.name}"),
        size = "100%"
      ))
      // Series
      override val series: SeriesCfg = js.Array[AnySeries](
        SeriesPie(name = "数据",
          data = js.Array[SeriesPieData](
            SeriesPieData(y = oldPerson, name = "老客户"),
            SeriesPieData(y = allPerson - oldPerson, name = "新客户")
          )
        )
      )
    }

    oldPeopleChart.innerHTML = ""

    renderChart(drawChart, oldPeopleChart)
  }

  def openWs(subId: String) = {

    val ws = new WebSocket(getWebsocketUrl(dom.document, subId))

    ws.onopen = { (e: Event) =>
      println(s"ws.onopen...${e.timeStamp}")
    }

    ws.onmessage = { (e: MessageEvent) =>

      val wsMsg = parse[WebSocketMsg](e.data.toString)

      wsMsg match {
        case Right(messages) =>

          messages match {
            case Heartbeat(_) =>
                //do nothing

            case msg@ComeIn(mac, time, oldOrNot) =>
              println(s"comeIn.i got a msg:$msg")
              val oldOldPerson = oldPerson
              val oldOnlinePerson = onlinePerson
              onLineMap.put(mac, time)
              inPerson +=  1
              onlinePerson += 1
              if(oldOrNot) oldPerson += 1
              areaDiv.innerHTML = ""
              areaDiv.appendChild(
                div(
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(onlinePerson),
                    p("区域内人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(inPerson),
                    p(s"进区域人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(outPerson),
                    p(s"出区域人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.id := "stayTime", *.cls := "info-blip glyphicon", *.fontSize := "xx-large")(
                      if(stayTime._2 > stayTime._1) stayTime._2/1000 else stayTime._1/1000
                    ),
                    p(s"驻留时长(s)")
                  )
                ).render
              )
              val newDiv = div(
                table(*.cls := "table")(
                  thead(
                    tr(
                      th(*.textAlign.center)("在线mac")
                    )
                  ),
                  tbody(*.textAlign.center)(
                    onLineMap.toList.map( m => makeRow(m._1))
                  )
                )
              ).render
              onLineDiv.innerHTML = ""
              onLineDiv.appendChild(newDiv)

              if(oldOldPerson / oldOnlinePerson != oldPerson / onlinePerson)
                drawOldChart(oldPerson, onlinePerson)

              jQuery("div[data-highcharts-chart=0]").each { (_: Int, e: dom.Element) =>
                jQuery(e).highcharts().foreach(_.series.apply(0).addPoint(options = SeriesSplineData(x = new Date(System.currentTimeMillis()).getTime() + (8 * 3600 * 1000) , y = onlinePerson), redraw = true, shift = true)).asInstanceOf[js.Any]
              }

            case msg@GetOut(macs, oldSum) =>
              println(s"i got a msg:$msg")
              val oldOldPerson = oldPerson
              val oldOnlinePerson = onlinePerson
              macs.map{ m => onLineMap.remove(m)}
              outPerson = outPerson + macs.length
              onlinePerson = onlinePerson - macs.length
              if(oldSum != 0) oldPerson = oldPerson - oldSum
              val timeTmp = System.currentTimeMillis() - onLineMap.toList.sortBy(_._2).head._2
              stayTime = (stayTime._1, timeTmp)
              areaDiv.innerHTML = ""
              areaDiv.appendChild(
                div(
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(onlinePerson),
                    p("区域内人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(inPerson),
                    p(s"进区域人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(outPerson),
                    p(s"出区域人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.id := "stayTime", *.cls := "info-blip glyphicon", *.fontSize := "xx-large")(
                      if(stayTime._2 > stayTime._1) stayTime._2/1000 else stayTime._1/1000
                    ),
                    p(s"驻留时长(s)")
                  )
                ).render
              )
              val newDiv = div(
                table(*.cls := "table")(
                  thead(
                    tr(
                      th(*.textAlign.center)("在线mac")
                    )
                  ),
                  tbody(*.textAlign.center)(
                    onLineMap.toList.map( m => makeRow(m._1))
                  )
                )
              ).render
              onLineDiv.innerHTML = ""
              onLineDiv.appendChild(newDiv)

              if(oldOldPerson / oldOnlinePerson != oldPerson / onlinePerson)
                drawOldChart(oldPerson, onlinePerson)

              jQuery("div[data-highcharts-chart=0]").each { (_: Int, e: dom.Element) =>
                jQuery(e).highcharts().foreach(_.series.apply(0).addPoint(options = SeriesSplineData(x = new Date(System.currentTimeMillis()).getTime() + (8 * 3600 * 1000) , y = onlinePerson), redraw = true, shift = true)).asInstanceOf[js.Any]
              }

            case msg@NowInfo(onlineSum, inSum, outSum, oldSum, pastOnline) =>
              println(s"i got a msg:$msg")
              onLineMap ++= onlineSum.map(a => (a._1, a._2))
              onlinePerson = onlineSum.length
              inPerson = inSum
              outPerson = outSum
              oldPerson = oldSum
              val timeTmp = System.currentTimeMillis() - onlineSum.sortBy(_._2).head._2
              stayTime = (timeTmp, timeTmp)
              areaDiv.innerHTML = ""
              areaDiv.appendChild(
                div(
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(onlinePerson),
                    p("区域内人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(inPerson),
                    p(s"进区域人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.cls := "info-blip glyphicon", *.fontSize := "xx-large")(outPerson),
                    p(s"出区域人数")
                  ),
                  div(*.cls := "col-md-3", *.textAlign := "center")(
                    div(*.id := "stayTime", *.cls := "info-blip glyphicon", *.fontSize := "xx-large")(stayTime._2/1000),
                    p(s"驻留时长(s)")
                  )
                ).render
              )
              val newDiv = div(
                table(*.cls := "table")(
                  thead(
                    tr(
                      th(*.textAlign.center)("在线mac")
                    )
                  ),
                  tbody(*.textAlign.center)(
                    onlineSum.map( m => makeRow(m._1))
                  )
                )
              ).render
              onLineDiv.innerHTML = ""
              onLineDiv.appendChild(newDiv)
              drawChart(pastOnline)
              drawOldChart(oldPerson, onlinePerson)
              Shortcut.schedule(scheduleTask, 1000)

            case x =>
              println(s"i got a msg:$x")
          }

        case Left(e) =>
          println(s"wsMsg match fail...$e")

      }
    }

    ws.onclose = {
      (e: Event) =>
        window.alert("ws 断开")
    }

  }

  private def scheduleTask() = {
    val stayDiv = dom.document.getElementById("stayTime").asInstanceOf[Div]
    val timeTmp = stayTime._2 + 1000
    val newTime = if(timeTmp >= stayTime._1) {
      stayTime = (timeTmp, timeTmp)
      stayTime._2 / 1000
    } else {
      stayTime = (stayTime._1, timeTmp)
      stayTime._1 / 1000
    }
    try{
      stayDiv.innerHTML = s"$newTime"
    } catch {
      case e:Exception =>
        println(s"$e")
    }
  }

  private def makeRow(mac: String) = {
    tr(
      td(mac)
    )
  }

  def drawChart(pastData: List[(Long, Int)]) = {

    val drawChart = new HighchartsConfig {

      // Chart config
      override val chart: Cfg[Chart] = Chart(`type` = "spline")

      // Chart title
      override val title: Cfg[Title] = Title(text = "动态实时数据")

      // X Axis settings
      override val xAxis: CfgArray[XAxis] = js.Array(XAxis(`type` = "datetime"))

      // Y Axis settings
      override val yAxis: CfgArray[YAxis] = js.Array(YAxis(title = YAxisTitle(text = "值"), plotLines = js.Array(YAxisPlotLines(value = 0.0, width = 0.5, color = "#808080"))))

      // Series
      override val series: SeriesCfg = js.Array[AnySeries](
        SeriesSpline(name = "数据",
          data = getdata(pastData).toJSArray
        )
      )
    }

    realTimeChart.innerHTML = ""

    renderChart(drawChart, realTimeChart)
  }

  private def getdata(pastData: List[(Long, Int)]) = {
    import scala.scalajs.js.Date

    pastData.sortBy(_._1).map{ i =>
      SeriesSplineData(x = new Date(i._1).getTime() + (8 * 3600 * 1000) , y = i._2)
    }
  }

  private def renderChart(chartConfig: CleanJsObject[js.Object], container:Div) = {
    dom.console.log(chartConfig)
    val newContainer = div().render
    jQuery(newContainer).highcharts(chartConfig)
    container.innerHTML = ""
    container.appendChild(newContainer)
  }

  def getWebsocketUrl(document: Document, subId: String): String = {
    val wsProtocol = if (dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://${dom.document.location.host}/flowShow/ws/home?subId=$subId"
  }

  def parse[T](s: String)(implicit decoder: Decoder[T]): Either[Error, T] = {
    import io.circe.parser._
    decode[T](s)
  }

  def makeGroupsSelects(list: List[Group]) = {

    def makeGroupSelects(group: Group) = {
      rangeIndex.appendChild(option(s"${group.name}").render)
    }

    list.map(data => makeGroupSelects(data))

  }

  def getGroupList() = {
    Http.getAndParse[GroupsRsp](Routes.getGroups).map { rsp =>
      if (rsp.errCode == 0) {
        GroupMap ++= rsp.data.map(g => (g.name, g))
        makeGroupsSelects(rsp.data)
      }
    }
  }

  override def locationHash = ""

  override protected def build(): Div = {
    getGroupList()
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-12", *.textAlign := "center")(
          h1(
            span(*.cls := "artpip-highlight", *.color := "#13C5E4")("区域实时客流呈现")
          )
        )
      ),
      searchByIdIncome,
      div(*.cls := "row", *.width := "100%", *.backgroundColor := "#282B3F")(
        div(*.cls := "col-md-8", *.backgroundColor := "#282B3F")(
          areaDiv,
          realTimeChart,
          oldPeopleChart
        ),
        onLineDiv
      )
    ).render
  }

}
