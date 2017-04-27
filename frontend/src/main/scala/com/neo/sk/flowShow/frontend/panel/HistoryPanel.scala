package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import com.neo.sk.flowShow.frontend.utils.{JsFunc, Http, MyUtil}
import com.neo.sk.flowShow.frontend.Routes
import io.circe.generic.auto._
import scalatags.JsDom.short._
import com.neo.sk.flowShow.ptcl._
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by whisky on 17/4/27.
  */
object HistoryPanel extends Panel{

  private val historyBox = ul(*.fontSize := "x-large").render

  private val macInput = input(*.cls := "form-control", *.`type` := "text", *.marginLeft := "5px").render

  private val searchByButton = button(*.cls := "btn btn-default", *.marginLeft := "5px")("查询").render

  private val searchByIdIncome =
    div(
      form(*.cls := "form-inline", *.marginLeft := "8%")(
        div(*.cls := "form-group")(
          label(*.`for` := "forMac")("用户mac"),
          macInput
        ),
        searchByButton
      )
    ).render

  private def dataHandler(data: List[UserHistory]) = {
    historyBox.innerHTML = ""
    val nowLocation = data.find(_.outTime.isEmpty)
    if(nowLocation.nonEmpty)
      historyBox.appendChild(li(s"用户当前位置:${nowLocation.get.groupName}").render)

    val historyLocation = data.filter(_.outTime.nonEmpty)
    historyLocation.map{ h =>
      historyBox.appendChild(
        li(
          s"${MyUtil.dataFormatDefault(h.inTime)} --- ${MyUtil.dataFormatDefault(h.outTime.get)}  位于 ${h.groupName}"
        ).render
      )
    }
  }

  searchByButton.onclick = { e: MouseEvent =>
    e.preventDefault()
    if(macInput.value == "")
      JsFunc.alert("查找信息不可为空!")
    else{
      val userMac = macInput.value
      Http.getAndParse[UserHistoryRsp](Routes.getHistory(userMac)).map{ rsp =>
        if(rsp.errCode == 0){
          dataHandler(rsp.data)
        } else {
          println(s"getUserHistory error: ${rsp.msg}")
        }
      }
    }
  }

  override def locationHash = ""

  override protected def build(): Div = {
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-12", *.textAlign := "center")(
          h1(
            span(*.cls := "artpip-highlight", *.color := "#13C5E4")("用户历史区域轨迹跟踪")
          )
        )
      ),
      searchByIdIncome,
      div(*.cls := "row", *.width := "70%", *.height := "50%", *.marginLeft := "10%", *.marginTop := "3%")(
        historyBox
      )
    ).render
  }

}
