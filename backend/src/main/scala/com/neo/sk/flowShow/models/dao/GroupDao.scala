package com.neo.sk.flowShow.models.dao

import com.neo.sk.flowShow.common.Constants
import com.neo.sk.flowShow.models.SlickTables._
import com.neo.sk.utils.DBUtil.db
import slick.jdbc.PostgresProfile.api._

/**
  * Created by whisky on 17/4/14.
  */
object GroupDao {

  def listDistributedGroups = db.run(
    tGroups.filter(_.groupId =!= Constants.defaultGroupId).result
  )

  def listGroupsByUserId(userId: Long) = db.run(
    tGroups.filter(c => (c.groupId =!= Constants.defaultGroupId) && (c.userId === userId)).result
  )

  def addGroup(name:String, duration:Long, userId:Long, timestamp:Long, map: Option[String], scala:Option[Double], width:Option[Double], height:Option[Double]) = db.run(
    tGroups.returning(tGroups.map(_.groupId)).+=(rGroups(-1l, name, userId, timestamp, duration, map, scala, width, height))
  )

  def modifyGroup(id:Long, name:String, duration:Long) = db.run(
    tGroups.filter(_.groupId === id).map(r => (r.groupName, r.durationLength)).update((name, duration))
  )

  def cleanLast() = db.run{
    tUserAction.filter(_.outTime.isEmpty === true).delete
  }

}
