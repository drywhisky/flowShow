package com.neo.sk.flowShow.frontend.panel

import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import com.neo.sk.flowShow.frontend.utils.{JsFunc, Http}
import com.neo.sk.flowShow.frontend.Routes
import scala.scalajs.js
import scalatags.JsDom.short._
import com.neo.sk.flowShow.ptcl._


/**
  * Created by whisky on 17/4/27.
  */
object HistoryPanel extends Panel{

  private val historyBox = div().render

  private val macInput = input(*.cls := "form-control", *.`type` := "text").render

  private val searchByButton = button(*.cls := "btn btn-default")("查询").render

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

  searchByButton.onclick = { e: MouseEvent =>
    e.preventDefault()
    if(macInput.value == "")
      JsFunc.alert("查找信息不可为空!")
    else{
      val userMac = macInput.value
      Http.getAndParse[UserHistoryRsp](Routes.getHistory).map{ rsp =>
        if(rsp.errCode == 0){

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
            span(*.cls := "artpip-highlight", *.color := "#13C5E4")("单盒实时客流呈现")
          )
        )
      ),
      searchByIdIncome,
      div(*.cls := "row", *.width := "70%", *.height := "50%", *.marginLeft := "20%", *.marginTop := "3%")(
        historyBox
      )
    ).render
  }

}
