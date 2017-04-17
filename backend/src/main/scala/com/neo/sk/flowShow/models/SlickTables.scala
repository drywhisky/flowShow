package com.neo.sk.flowShow.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tBoxs.schema ++ tBrand.schema ++ tCountDetail.schema ++ tCountHistory.schema ++ tGroups.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tBoxs
    *  @param boxId Database column box_id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param boxName Database column box_name SqlType(varchar), Length(255,true)
    *  @param boxMac Database column box_mac SqlType(varchar), Length(63,true)
    *  @param createTime Database column create_time SqlType(int8)
    *  @param groupId Database column group_id SqlType(int8), Default(0)
    *  @param rssiSet Database column rssi_set SqlType(int4) */
  case class rBoxs(boxId: Long, boxName: String, boxMac: String, createTime: Long, groupId: Long = 0L, rssiSet: Int)
  /** GetResult implicit for fetching rBoxs objects using plain SQL queries */
  implicit def GetResultrBoxs(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rBoxs] = GR{
    prs => import prs._
      rBoxs.tupled((<<[Long], <<[String], <<[String], <<[Long], <<[Long], <<[Int]))
  }
  /** Table description of table boxs. Objects of this class serve as prototypes for rows in queries. */
  class tBoxs(_tableTag: Tag) extends profile.api.Table[rBoxs](_tableTag, "boxs") {
    def * = (boxId, boxName, boxMac, createTime, groupId, rssiSet) <> (rBoxs.tupled, rBoxs.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(boxId), Rep.Some(boxName), Rep.Some(boxMac), Rep.Some(createTime), Rep.Some(groupId), Rep.Some(rssiSet)).shaped.<>({r=>import r._; _1.map(_=> rBoxs.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column box_id SqlType(bigserial), AutoInc, PrimaryKey */
    val boxId: Rep[Long] = column[Long]("box_id", O.AutoInc, O.PrimaryKey)
    /** Database column box_name SqlType(varchar), Length(255,true) */
    val boxName: Rep[String] = column[String]("box_name", O.Length(255,varying=true))
    /** Database column box_mac SqlType(varchar), Length(63,true) */
    val boxMac: Rep[String] = column[String]("box_mac", O.Length(63,varying=true))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
    /** Database column group_id SqlType(int8), Default(0) */
    val groupId: Rep[Long] = column[Long]("group_id", O.Default(0L))
    /** Database column rssi_set SqlType(int4) */
    val rssiSet: Rep[Int] = column[Int]("rssi_set")
  }
  /** Collection-like TableQuery object for table tBoxs */
  lazy val tBoxs = new TableQuery(tag => new tBoxs(tag))

  /** Entity class storing rows of table tBrand
    *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
    *  @param name Database column name SqlType(varchar), Length(255,true), Default() */
  case class rBrand(id: Int, name: String = "")
  /** GetResult implicit for fetching rBrand objects using plain SQL queries */
  implicit def GetResultrBrand(implicit e0: GR[Int], e1: GR[String]): GR[rBrand] = GR{
    prs => import prs._
      rBrand.tupled((<<[Int], <<[String]))
  }
  /** Table description of table brand. Objects of this class serve as prototypes for rows in queries. */
  class tBrand(_tableTag: Tag) extends profile.api.Table[rBrand](_tableTag, "brand") {
    def * = (id, name) <> (rBrand.tupled, rBrand.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> rBrand.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true), O.Default(""))
  }
  /** Collection-like TableQuery object for table tBrand */
  lazy val tBrand = new TableQuery(tag => new tBrand(tag))

  /** Entity class storing rows of table tCountDetail
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param groupId Database column group_id SqlType(varchar), Length(255,true)
    *  @param timestamp Database column timestamp SqlType(int8)
    *  @param count Database column count SqlType(int4) */
  case class rCountDetail(id: Long, groupId: String, timestamp: Long, count: Int)
  /** GetResult implicit for fetching rCountDetail objects using plain SQL queries */
  implicit def GetResultrCountDetail(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rCountDetail] = GR{
    prs => import prs._
      rCountDetail.tupled((<<[Long], <<[String], <<[Long], <<[Int]))
  }
  /** Table description of table count_detail. Objects of this class serve as prototypes for rows in queries. */
  class tCountDetail(_tableTag: Tag) extends profile.api.Table[rCountDetail](_tableTag, "count_detail") {
    def * = (id, groupId, timestamp, count) <> (rCountDetail.tupled, rCountDetail.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(groupId), Rep.Some(timestamp), Rep.Some(count)).shaped.<>({r=>import r._; _1.map(_=> rCountDetail.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column group_id SqlType(varchar), Length(255,true) */
    val groupId: Rep[String] = column[String]("group_id", O.Length(255,varying=true))
    /** Database column timestamp SqlType(int8) */
    val timestamp: Rep[Long] = column[Long]("timestamp")
    /** Database column count SqlType(int4) */
    val count: Rep[Int] = column[Int]("count")
  }
  /** Collection-like TableQuery object for table tCountDetail */
  lazy val tCountDetail = new TableQuery(tag => new tCountDetail(tag))

  /** Entity class storing rows of table tCountHistory
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param groupId Database column group_id SqlType(varchar), Length(255,true)
    *  @param timestamp Database column timestamp SqlType(int8)
    *  @param count Database column count SqlType(int4) */
  case class rCountHistory(id: Long, groupId: String, timestamp: Long, count: Int)
  /** GetResult implicit for fetching rCountHistory objects using plain SQL queries */
  implicit def GetResultrCountHistory(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rCountHistory] = GR{
    prs => import prs._
      rCountHistory.tupled((<<[Long], <<[String], <<[Long], <<[Int]))
  }
  /** Table description of table count_history. Objects of this class serve as prototypes for rows in queries. */
  class tCountHistory(_tableTag: Tag) extends profile.api.Table[rCountHistory](_tableTag, "count_history") {
    def * = (id, groupId, timestamp, count) <> (rCountHistory.tupled, rCountHistory.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(groupId), Rep.Some(timestamp), Rep.Some(count)).shaped.<>({r=>import r._; _1.map(_=> rCountHistory.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column group_id SqlType(varchar), Length(255,true) */
    val groupId: Rep[String] = column[String]("group_id", O.Length(255,varying=true))
    /** Database column timestamp SqlType(int8) */
    val timestamp: Rep[Long] = column[Long]("timestamp")
    /** Database column count SqlType(int4) */
    val count: Rep[Int] = column[Int]("count")
  }
  /** Collection-like TableQuery object for table tCountHistory */
  lazy val tCountHistory = new TableQuery(tag => new tCountHistory(tag))

  /** Entity class storing rows of table tGroups
    *  @param groupId Database column group_id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param groupName Database column group_name SqlType(varchar), Length(255,true)
    *  @param createTime Database column create_time SqlType(int8), Default(Some(0))
    *  @param durationLength Database column duration_length SqlType(int8) */
  case class rGroups(groupId: Long, groupName: String, createTime: Option[Long] = Some(0L), durationLength: Long)
  /** GetResult implicit for fetching rGroups objects using plain SQL queries */
  implicit def GetResultrGroups(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]]): GR[rGroups] = GR{
    prs => import prs._
      rGroups.tupled((<<[Long], <<[String], <<?[Long], <<[Long]))
  }
  /** Table description of table groups. Objects of this class serve as prototypes for rows in queries. */
  class tGroups(_tableTag: Tag) extends profile.api.Table[rGroups](_tableTag, "groups") {
    def * = (groupId, groupName, createTime, durationLength) <> (rGroups.tupled, rGroups.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(groupId), Rep.Some(groupName), createTime, Rep.Some(durationLength)).shaped.<>({r=>import r._; _1.map(_=> rGroups.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column group_id SqlType(bigserial), AutoInc, PrimaryKey */
    val groupId: Rep[Long] = column[Long]("group_id", O.AutoInc, O.PrimaryKey)
    /** Database column group_name SqlType(varchar), Length(255,true) */
    val groupName: Rep[String] = column[String]("group_name", O.Length(255,varying=true))
    /** Database column create_time SqlType(int8), Default(Some(0)) */
    val createTime: Rep[Option[Long]] = column[Option[Long]]("create_time", O.Default(Some(0L)))
    /** Database column duration_length SqlType(int8) */
    val durationLength: Rep[Long] = column[Long]("duration_length")
  }
  /** Collection-like TableQuery object for table tGroups */
  lazy val tGroups = new TableQuery(tag => new tGroups(tag))
}
