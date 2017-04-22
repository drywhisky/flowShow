package com.neo.sk.flowShow.frontend.pages

import com.neo.sk.flowShow.frontend.utils.Page
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.KeyboardEvent
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc, Shortcut}
import com.neo.sk.flowShow.ptcl._
import com.neo.sk.flowShow.frontend.Routes
import io.circe.syntax._
import io.circe.generic.auto._
import org.scalajs.dom
import scalatags.JsDom.short._

import scala.concurrent.ExecutionContext.Implicits.global
import scalatags.JsDom.short._

/**
  * Created by whisky on 17/4/22.
  */
object RegisterPage extends Page{

  override def locationHash: String = ""

  private val registerUrl = Routes.registerSubmit

  private val account = input(*.cls := "form-control", *.`type` := "text", *.placeholder := "account").render
  private val psw = input(*.cls := "form-control", *.`type` := "password", *.placeholder := "password").render
  private val confirmPsw = input(*.cls := "form-control", *.`type` := "password", *.placeholder := "confirm the password").render


  private val submitButton = input(*.cls := "btn btn-primary btn-block", *.`type` := "submit", *.value := "Register")("登录").render

  private val goBack = a("Back").render

  submitButton.onclick = { e: MouseEvent =>
    val nickName = account.value
    val pswValue = psw.value
    val confirmPswValue = confirmPsw.value
    if (confirmPswValue == pswValue) {
      if (nickName != "" && pswValue != "") {
        val bodyStr = UserRegisterInfo(nickName, pswValue).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](registerUrl, bodyStr).map { rsp =>
          if (rsp.errCode == 0) {
            println(s"register request sent success, result: $rsp")
            Shortcut.redirect(Routes.home)
          }
          else {
            dom.window.alert(s"error.${rsp.msg}")
          }
        }
      }
      else{
        dom.window.alert(s"信息不可为空")
      }
    }
    else{
      dom.window.alert(s"两次输入的密码不同")
    }
  }

  goBack.onclick = { e:MouseEvent =>
    e.preventDefault()
    Shortcut.redirect(Routes.login)
  }

  override protected def build(): Div = {
    div(*.cls := "login-box")(
      div(*.cls := "login-logo")(
        h1(*.color := "white")("实时客流可视化系统")
      ),
      div(*.cls := "login-box-body")(
        p(*.cls := "login-box-msg")("Register a new membership"),
        form(*.cls := "form-horizontal")(
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(account)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(psw)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(confirmPsw)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-sm-12")(submitButton)
          ),
          div(*.cls := "form-group", *.textAlign.center)(
            div(*.cls := "col-xs-4 text-left")(goBack)
          )
        )
      )
    ).render
  }

}
