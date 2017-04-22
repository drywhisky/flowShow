package com.neo.sk.flowShow.frontend.panel


import com.neo.sk.flowShow.frontend.Routes
import com.neo.sk.flowShow.frontend.utils.Panel
import org.scalajs.dom.html.Div
import io.circe.generic.auto._
import io.circe.syntax._
import scalatags.JsDom.short._
import com.neo.sk.flowShow.frontend.utils.{Http, JsFunc}
import com.neo.sk.flowShow.ptcl._
import io.circe.generic.auto._
import org.scalajs.dom.MouseEvent
import com.neo.sk.flowShow.frontend.utils.{MyUtil, Modal}
import scala.collection.mutable
import scala.scalajs.js.Date
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by whisky on 17/4/22.
  */
object GroupPanel extends Panel{

  import io.circe.generic.auto._


  override def locationHash = ""

  private val currentDiv = div().render

  private val tmp = GroupListPanel.render

  val GroupMap = mutable.HashMap[Long, Group]()

  val BoxMap = mutable.HashMap[Long, Box]()

  currentDiv.appendChild(tmp)

  def SetContent(component:Div) = {
    currentDiv.textContent = ""
    currentDiv.appendChild(component)
  }

  override def build():Div = {
    currentDiv
  }


}

object GroupListPanel extends Panel{

  private val groupList = div(div()).render

  private val editBox = div().render

  private val createCategoryButton = button(*.cls := "btn btn-warning")("+添加区域").render


  def editAction(id: Long, name: String, time: Long, durationLength: Long) = {

    val modalId = input(*.`type` := "text", *.cls := "form-control", *.value := id, *.disabled := true).render
    val modalName = input(*.`type` := "text", *.cls := "form-control", *.value := name).render
    val modalTime = input(*.`type` := "text", *.cls := "form-control", *.value := MyUtil.DateFormatter(new Date(time), "YYYY-MM-DD hh:mm:ss"), *.disabled := true).render
    val modalDua = input(*.`type` := "text", *.cls := "form-control", *.value := durationLength).render


    val header = div(*.cls := "modal-title")("更新区域信息")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("区域id"),
          div(*.cls := "col-md-4")(modalId)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("区域名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("创建时间"),
          div(*.cls := "col-md-4")(modalTime)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("预设驻留时长"),
          div(*.cls := "col-md-4")(modalDua)
        )
      )
    )

    def clickFunction():Unit = {
      if (modalName.value == "" || modalDua.value == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = ModifyGroup(id, modalName.value, modalDua.value.toLong).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](Routes.modifyGroup, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            GroupPanel.GroupMap.update(id, Group(id, modalName.value, time, modalDua.value.toLong))
            makeGroupList(GroupPanel.GroupMap)
          } else {
            JsFunc.alert(s"error: ${rsp.msg}")
          }
        }
      }
    }

    val model = new Modal(
      header.render,
      body.render,
      clickFunction,
      ""
    )

    model.show
    editBox.textContent = ""
    editBox.appendChild(model.render)
  }

  private def makeGroupList(groups: mutable.HashMap[Long, Group]) = {

    def makeRow(group: Group) = {
      val editButton = button(*.cls := "btn btn-info")("编辑").render

      editButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        editAction(group.id, group.name, group.createTime, group.durationLength)
      }

      tr(
        td(group.id),
        td(group.name),
        td(MyUtil.DateFormatter(new Date(group.createTime), "YYYY-MM-DD hh:mm:ss")),
        td(group.durationLength),
        td(editButton)
      )
    }

    val newDiv = div(
      table(*.cls := "table")(
        thead(
          tr(
            th(*.textAlign.center)("#"),
            th(*.textAlign.center)("区域名称"),
            th(*.textAlign.center)("创建时间"),
            th(*.textAlign.center)("预设驻留时长"),
            th(*.textAlign.center)("操作")
          )
        ),
        tbody(*.textAlign.center)(
          groups.toList.map(_._2).map(m => makeRow(m))
        )
      )
    ).render

    groupList.replaceChild(newDiv, groupList.firstChild)
  }

  private def getGroups() = {
    Http.getAndParse[GroupsRsp](Routes.getGroups).map { rsp =>
      if (rsp.errCode == 0) {
        println(s"success")
        GroupPanel.GroupMap ++= rsp.data.map( g => (g.id, g))
        makeGroupList(GroupPanel.GroupMap)
      } else {
        println(s"getCategoryList error: ${rsp.msg}")
      }
    }
  }

  override def locationHash = ""

  override protected def build() : Div = {
    getGroups()
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-2")(h3("区域管理")),
        div(*.cls := "col-md-2 col-md-offset-8")(createCategoryButton)
      ),
      div(*.cls := "row",*.style := "margin-top:20px;")(
        groupList
      ),
      div(*.cls := "row",*.style := "margin-top:20px;")(
        editBox
      )
    ).render
  }

}
