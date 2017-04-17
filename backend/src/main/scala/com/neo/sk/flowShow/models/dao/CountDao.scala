package com.neo.sk.flowShow.models.dao

import com.neo.sk.flowShow.models.SlickTables._
import com.neo.sk.utils.DBUtil.db
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by whisky on 17/4/17.
  */
object CountDao {

  def addCountDetail(groupId: String, timeSet: Set[Long], count: Map[Long, Int]) = {
    val record = count.map(c => rCountDetail(-1L,groupId,c._1,c._2))
    for{
      _ <- db.run(tCountDetail.filter(c => c.timestamp.inSet(timeSet) && c.groupId === groupId).delete)
      _ <- db.run(tCountDetail ++= record)
    }yield ()
  }

  def getCountDetailByInterval(groupId: String, startTime: Long, endTime: Long) = db.run{
    tCountDetail.filter(r => r.groupId === groupId && r.timestamp >= startTime && r.timestamp < endTime).result
  }

}
