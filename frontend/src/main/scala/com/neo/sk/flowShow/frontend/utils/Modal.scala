package com.neo.sk.flowShow.frontend.utils

import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div

/**
  * Created by whisky on 17/4/22.
  */
class Modal(header:Div, child:Div, successFunc :()=> Unit, id:String) extends Component[Div] {

  import scalatags.JsDom.short._

  def show: Unit = {
    modal.setAttribute("style","display:block")
  }

  def hide: Unit = {
    modal.setAttribute("style","display:none")
  }

  def noButton: Unit = {
    confirmButton.setAttribute("style","display:none")
    cancelButton.setAttribute("style","display:none")
  }

  val confirmButton = button(*.cls:="btn btn-primary", *.data("dismiss") := "modal", *.aria.label := "close")("确认").render

  confirmButton.onclick = {
    e:MouseEvent =>
      e.preventDefault()
      successFunc()
      hide
  }

  val cancelButton = button(*.cls:="btn btn-primary", *.data("dismiss") := "modal", *.aria.label := "close")("取消").render

  val closeButton = button(*.cls := "close", *.`type` := "button", *.data("dismiss") := "modal", *.aria.label := "close")(
    span(*.aria.hidden := "true")("×")
  ).render

  cancelButton.onclick = { e:MouseEvent =>
    e.preventDefault()
    hide
  }

  closeButton.onclick = { e:MouseEvent =>
    e.preventDefault()
    hide
  }

  val modal = div(*.cls:="modal", *.id:= id,*.tabindex := "-1", *.role := "dialog", *.aria.labelledby := "myModalLabel",*.display.none)(
    div(*.cls := "modal-dialog", *.role := "document")(
      div(*.cls := "modal-content")(
        div(*.cls:="modal-header")(
          closeButton,
          header.render
        ),
        div(*.cls:="modal-body")(
          child.render
        ),
        div(*.cls:="modal-footer")(
          confirmButton,cancelButton
        )
      )
    )
  ).render

  override def build() : Div = {
    modal
  }

}
