package com.neo.sk.flowShow.frontend

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 6:49 PM
  */
object Routes {


  val baseUrl = "/flowShow/user"

  val login = baseUrl + "/login"
  val loginSubmit = baseUrl + "/loginSubmit"
  val registerSubmit = baseUrl + "/registerSubmit"
  val home = baseUrl + "/home"
  val register = baseUrl + "/register"

  val imageUpload = baseUrl + "/imageUpload"
  val getGroups = baseUrl + "/getGroups"
  val modifyGroup = baseUrl + "/modifyGroup"
  val addGroup = baseUrl + "/addGroup"

  val getAllBoxs = baseUrl + "/getAllBoxs"
  def getBoxs(groupId:Long) = {
    baseUrl + s"/getBoxs?groupId=$groupId"
  }
  val modifyBox = baseUrl + "/modifyBox"
  val addBox = baseUrl + "/addBox"

  def getHistory(mac:String) = {
    baseUrl + s"/getHistory?mac=$mac"
  }

  val addStaff = baseUrl + "/addStaff"
  val deleteStaff = baseUrl + "/deleteStaff"
  def getAllStaff(groupId: Long) = {
    baseUrl + s"/getAllStaff?groupId=$groupId"
  }


}
