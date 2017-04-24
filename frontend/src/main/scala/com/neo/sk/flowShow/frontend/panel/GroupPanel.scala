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
import org.scalajs.dom.{Event, MouseEvent}
import com.neo.sk.flowShow.frontend.utils.{Modal, MyUtil}
import org.scalajs.dom.raw.{FormData, XMLHttpRequest}

import scala.collection.mutable
import scala.scalajs.js.Date
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by whisky on 17/4/22.
  */
object GroupPanel extends Panel{

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

  private val createGroupButton = button(*.cls := "btn btn-warning")("+添加区域").render

  createGroupButton.onclick = { e: MouseEvent =>
    e.preventDefault()

    val modalName = input(*.`type` := "text", *.cls := "form-control").render
    val modalDua = input(*.`type` := "text", *.cls := "form-control").render


    val header = div(*.cls := "modal-title")("新建区域")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("区域名称"),
          div(*.cls := "col-md-4")(modalName)
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
        val data = AddGroup(modalName.value, modalDua.value.toLong).asJson.noSpaces
        Http.postJsonAndParse[AddGroupRsp](Routes.addGroup, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            val newGroup = Group(rsp.id.getOrElse(0l), modalName.value, rsp.timestamp.getOrElse(0l), modalDua.value.toLong)
            GroupPanel.GroupMap.put(newGroup.id, newGroup)
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


  private def editAction(id: Long, name: String, time: Long, durationLength: Long) = {

    val modalId = input(*.`type` := "text", *.cls := "form-control", *.value := id, *.disabled := true).render
    val modalName = input(*.`type` := "text", *.cls := "form-control", *.value := name).render
    val modalTime = input(*.`type` := "text", *.cls := "form-control", *.value := MyUtil.DateFormatter(new Date(time), "YYYY-MM-DD hh:mm:ss"), *.disabled := true).render
    val modalDua = input(*.`type` := "text", *.cls := "form-control", *.value := durationLength).render
    val uploadButton = button(*.cls := "\"btn btn-warning",*.fontSize.small)("上传图片").render

    uploadButton.onclick = {
      e:MouseEvent =>
        e.preventDefault()
        upload()
    }

    val imageUpload =
      form(*.enctype:="multipart/form-data",*.action:=Routes.imageUpload+imageUpload, *.method:="post", *.display.inline)(
        input(*.`type`:="file",*.name:="fileUpload")
      ).render

    def upload() = {
      println(s"upload execute !")
      val oData = new FormData(imageUpload)
      println(s"oData = $oData")
      val oReq = new XMLHttpRequest()
      val a = oReq.open("POST", Routes.imageUpload, true)
      println(s"a = $a")

      oReq.onload = { e: Event =>
        e.preventDefault()
        if (oReq.status == 200) {
          JsFunc.alert("上传成功！")
        } else {
          JsFunc.alert("上传失败！")
        }
      }
      oReq.send(oData)
    }

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
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("区域地图"),
          div(*.cls := "col-md-4")(uploadButton)
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

  private def makeGroupList(groups: mutable.HashMap[Long, Group]) : Unit= {

    def makeRow(group: Group) = {
      val editButton = button(*.cls := "btn btn-info")("编辑").render
      val boxButton = button(*.cls := "btn btn-info")("查看盒子").render


      editButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        editAction(group.id, group.name, group.createTime, group.durationLength)
      }

      boxButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        GroupPanel.SetContent(new BoxListPanel(group.id, group.name).render)
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
            th(*.textAlign.center)("名称"),
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
        div(*.cls := "col-md-2 col-md-offset-8")(createGroupButton)
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

class BoxListPanel(groupId: Long, name: String) extends Panel {

  private val createBoxButton = button(*.cls := "btn btn-warning")("+添加盒子").render

  private val boxList = div(div()).render

  private val editBox = div().render

  createBoxButton.onclick = { e: MouseEvent =>

    val modalName = input(*.`type` := "text", *.cls := "form-control").render
    val modalMac = input(*.`type` := "text", *.cls := "form-control").render
    val modalRssi = input(*.`type` := "text", *.cls := "form-control").render

    val header = div(*.cls := "modal-title")("新建盒子")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("盒子名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("盒子mac"),
          div(*.cls := "col-md-4")(modalMac)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("预设rssi"),
          div(*.cls := "col-md-4")(modalRssi)
        )
      )
    )

    def clickFunction():Unit = {
      if (modalName.value == "" || modalMac.value == "" || modalRssi.value == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = AddBox(modalName.value, modalMac.value, modalRssi.value.toInt, groupId).asJson.noSpaces
        Http.postJsonAndParse[AddGroupRsp](Routes.addBox, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            val newBox = Box(rsp.id.getOrElse(0l), modalName.value, modalMac.value, rsp.timestamp.getOrElse(0l), modalRssi.value.toInt)
            GroupPanel.BoxMap.put(newBox.id, newBox)
            makeBoxList(GroupPanel.BoxMap)
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

  private def editAction(id: Long, name: String, mac:String, time: Long, rssi: Int) : Unit= {

    val modalId = input(*.`type` := "text", *.cls := "form-control", *.value := id, *.disabled := true).render
    val modalMac = input(*.`type` := "text", *.cls := "form-control", *.value := mac, *.disabled := true).render
    val modalName = input(*.`type` := "text", *.cls := "form-control", *.value := name).render
    val modalTime = input(*.`type` := "text", *.cls := "form-control", *.value := MyUtil.DateFormatter(new Date(time), "YYYY-MM-DD hh:mm:ss"), *.disabled := true).render
    val modalRssi = input(*.`type` := "text", *.cls := "form-control", *.value := rssi).render


    val header = div(*.cls := "modal-title")("更新区域信息")
    val body = div(*.cls := "row", *.textAlign.center)(
      form(*.cls := "form-horizontal", *.textAlign.center, *.style := "margin-top:10px;")(
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("盒子id"),
          div(*.cls := "col-md-4")(modalId)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("盒子名称"),
          div(*.cls := "col-md-4")(modalName)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("盒子mac"),
          div(*.cls := "col-md-4")(modalMac)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("创建时间"),
          div(*.cls := "col-md-4")(modalTime)
        ),
        div(*.cls := "form-group", *.textAlign.center)(
          label(*.cls := "col-md-2 col-md-offset-2 control-label")("预设rssi"),
          div(*.cls := "col-md-4")(modalRssi)
        )
      )
    )

    def clickFunction():Unit = {
      if (modalName.value == "" || modalRssi.value == "") {
        JsFunc.alert(s"error!")
      } else {
        val data = ModifyBox(id, mac, modalName.value, modalRssi.value.toInt).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](Routes.modifyBox, data).map { rsp =>
          if (rsp.errCode == 0) {
            JsFunc.alert(s"success")
            GroupPanel.BoxMap.update(id, Box(id, modalName.value, mac, time, modalRssi.value.toInt))
            makeBoxList(GroupPanel.BoxMap)
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


  private def makeBoxList(boxs: mutable.HashMap[Long, Box]) : Unit = {

    def makeRow(box: Box) = {
      val editButton = button(*.cls := "btn btn-info")("编辑").render

      editButton.onclick = { e: MouseEvent =>
        e.preventDefault()
        editAction(box.id, box.name, box.mac, box.createTime, box.rssi)
      }

      tr(
        td(box.id),
        td(box.name),
        td(box.mac),
        td(MyUtil.DateFormatter(new Date(box.createTime), "YYYY-MM-DD hh:mm:ss")),
        td(box.rssi),
        td(editButton)
      )
    }

    val newDiv = div(
      table(*.cls := "table")(
        thead(
          tr(
            th(*.textAlign.center)("#"),
            th(*.textAlign.center)("名称"),
            th(*.textAlign.center)("mac"),
            th(*.textAlign.center)("创建时间"),
            th(*.textAlign.center)("预设rssi"),
            th(*.textAlign.center)("操作")
          )
        ),
        tbody(*.textAlign.center)(
          boxs.toList.map(_._2).map(m => makeRow(m))
        )
      )
    ).render

    boxList.replaceChild(newDiv, boxList.firstChild)
  }


  override def locationHash = ""

  private def getBox(groupId: Long) = {
    Http.getAndParse[BoxsRsp](Routes.getBoxs(groupId)).map { rsp =>
      if (rsp.errCode == 0) {
        println(s"success")
        GroupPanel.BoxMap ++= rsp.data.map(g => (g.id, g))
        makeBoxList(GroupPanel.BoxMap)
      } else {
        println(s"getCategoryList error: ${rsp.msg}")
      }
    }
  }

  override protected def build(): Div = {
    getBox(groupId)
    div(
      div(*.cls := "row")(
        div(*.cls := "col-md-2")(h3(s"${name}盒子管理")),
        div(*.cls := "col-md-2 col-md-offset-8")(createBoxButton)
      ),
      div(*.cls := "row", *.style := "margin-top:20px;")(
        boxList
      ),
      div(*.cls := "row", *.style := "margin-top:20px;")(
        editBox
      )
    ).render
  }

}
