package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.utils.Page
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.KeyboardEvent

import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc, Shortcut}
import com.neo.sk.flowShow.ptcl
import com.neo.sk.flowShow.frontend.Routes
import io.circe.syntax._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext.Implicits.global
import scalatags.JsDom.short._


/**
  * Created by whisky on 17/4/22.
  */
object LoginPage extends Page{

  override def locationHash: String = ""

  private val account = input(*.`type` := "text", *.cls := "form-control", *.placeholder := "account").render

  private val pwd = input(*.`type` := "password", *.cls := "form-control", *.placeholder := "psaaword").render

  pwd.onkeypress = {
    e: KeyboardEvent =>
      if (e.charCode == KeyCode.Enter) {
        e.preventDefault()
        submitButton.click()
      }
  }

  private val newAccount = a(*.href := "")("New account").render

  private val submitButton = input(*.cls := "btn btn-primary btn-block", *.`type` := "submit", *.value := "Sign in")("登录").render

  submitButton.onclick = { e: MouseEvent =>
    e.preventDefault()
    val data = ptcl.UserConfirm(account.value, pwd.value).asJson.noSpaces
    Http.postJsonAndParse[ptcl.CommonRsp](Routes.loginSubmit, data).map { rsp =>
      if (rsp.errCode == 0) {
        Shortcut.redirect("./home")
      } else {
        JsFunc.alert(s"error: ${rsp.msg}")
      }
    }
  }

  newAccount.onclick = { e:MouseEvent =>
    e.preventDefault()
    Shortcut.redirect(Routes.register)
  }

  override protected def build(): Div = {
    div(*.cls := "login-box")(
      div(*.cls := "login-logo")(
        h1(*.color := "white")("实时客流可视化系统")
      ),
      div(*.cls := "login-box-body")(
        p(*.cls := "login-box-msg")("Sign in to start your session"),
        form(*.cls := "form-horizontal")(
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(account)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(pwd)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(submitButton)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-xs-4 text-left")(newAccount)
          )
        )
      )
    ).render
  }

}
