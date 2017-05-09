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
  lazy val schema: profile.SchemaDescription = Array(tBoxs.schema, tCountDetail.schema, tGroups.schema, tStaffMac.schema, tUserAction.schema, tUsers.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tBoxs
    *  @param boxId Database column box_id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param boxName Database column box_name SqlType(varchar), Length(255,true)
    *  @param boxMac Database column box_mac SqlType(varchar), Length(63,true)
    *  @param createTime Database column create_time SqlType(int8)
    *  @param userId Database column user_id SqlType(int8)
    *  @param groupId Database column group_id SqlType(int8), Default(0)
    *  @param rssiSet Database column rssi_set SqlType(int4)
    *  @param x Database column x SqlType(float8), Default(None)
    *  @param y Database column y SqlType(float8), Default(None) */
  case class rBoxs(boxId: Long, boxName: String, boxMac: String, createTime: Long, userId: Long, groupId: Long = 0L, rssiSet: Int, x: Option[Double] = None, y: Option[Double] = None)
  /** GetResult implicit for fetching rBoxs objects using plain SQL queries */
  implicit def GetResultrBoxs(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[Option[Double]]): GR[rBoxs] = GR{
    prs => import prs._
      rBoxs.tupled((<<[Long], <<[String], <<[String], <<[Long], <<[Long], <<[Long], <<[Int], <<?[Double], <<?[Double]))
  }
  /** Table description of table boxs. Objects of this class serve as prototypes for rows in queries. */
  class tBoxs(_tableTag: Tag) extends profile.api.Table[rBoxs](_tableTag, "boxs") {
    def * = (boxId, boxName, boxMac, createTime, userId, groupId, rssiSet, x, y) <> (rBoxs.tupled, rBoxs.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(boxId), Rep.Some(boxName), Rep.Some(boxMac), Rep.Some(createTime), Rep.Some(userId), Rep.Some(groupId), Rep.Some(rssiSet), x, y).shaped.<>({r=>import r._; _1.map(_=> rBoxs.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column box_id SqlType(bigserial), AutoInc, PrimaryKey */
    val boxId: Rep[Long] = column[Long]("box_id", O.AutoInc, O.PrimaryKey)
    /** Database column box_name SqlType(varchar), Length(255,true) */
    val boxName: Rep[String] = column[String]("box_name", O.Length(255,varying=true))
    /** Database column box_mac SqlType(varchar), Length(63,true) */
    val boxMac: Rep[String] = column[String]("box_mac", O.Length(63,varying=true))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column group_id SqlType(int8), Default(0) */
    val groupId: Rep[Long] = column[Long]("group_id", O.Default(0L))
    /** Database column rssi_set SqlType(int4) */
    val rssiSet: Rep[Int] = column[Int]("rssi_set")
    /** Database column x SqlType(float8), Default(None) */
    val x: Rep[Option[Double]] = column[Option[Double]]("x", O.Default(None))
    /** Database column y SqlType(float8), Default(None) */
    val y: Rep[Option[Double]] = column[Option[Double]]("y", O.Default(None))
  }
  /** Collection-like TableQuery object for table tBoxs */
  lazy val tBoxs = new TableQuery(tag => new tBoxs(tag))

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

  /** Entity class storing rows of table tGroups
    *  @param groupId Database column group_id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param groupName Database column group_name SqlType(varchar), Length(255,true)
    *  @param userId Database column user_id SqlType(int8)
    *  @param createTime Database column create_time SqlType(int8)
    *  @param durationLength Database column duration_length SqlType(int8)
    *  @param map Database column map SqlType(varchar), Length(255,true), Default(None)
    *  @param scala Database column scala SqlType(float8), Default(None)
    *  @param width Database column width SqlType(float8), Default(None)
    *  @param height Database column height SqlType(float8), Default(None) */
  case class rGroups(groupId: Long, groupName: String, userId: Long, createTime: Long, durationLength: Long, map: Option[String] = None, scala: Option[Double] = None, width: Option[Double] = None, height: Option[Double] = None)
  /** GetResult implicit for fetching rGroups objects using plain SQL queries */
  implicit def GetResultrGroups(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Option[Double]]): GR[rGroups] = GR{
    prs => import prs._
      rGroups.tupled((<<[Long], <<[String], <<[Long], <<[Long], <<[Long], <<?[String], <<?[Double], <<?[Double], <<?[Double]))
  }
  /** Table description of table groups. Objects of this class serve as prototypes for rows in queries. */
  class tGroups(_tableTag: Tag) extends profile.api.Table[rGroups](_tableTag, "groups") {
    def * = (groupId, groupName, userId, createTime, durationLength, map, scala, width, height) <> (rGroups.tupled, rGroups.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(groupId), Rep.Some(groupName), Rep.Some(userId), Rep.Some(createTime), Rep.Some(durationLength), map, scala, width, height).shaped.<>({r=>import r._; _1.map(_=> rGroups.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column group_id SqlType(bigserial), AutoInc, PrimaryKey */
    val groupId: Rep[Long] = column[Long]("group_id", O.AutoInc, O.PrimaryKey)
    /** Database column group_name SqlType(varchar), Length(255,true) */
    val groupName: Rep[String] = column[String]("group_name", O.Length(255,varying=true))
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
    /** Database column duration_length SqlType(int8) */
    val durationLength: Rep[Long] = column[Long]("duration_length")
    /** Database column map SqlType(varchar), Length(255,true), Default(None) */
    val map: Rep[Option[String]] = column[Option[String]]("map", O.Length(255,varying=true), O.Default(None))
    /** Database column scala SqlType(float8), Default(None) */
    val scala: Rep[Option[Double]] = column[Option[Double]]("scala", O.Default(None))
    /** Database column width SqlType(float8), Default(None) */
    val width: Rep[Option[Double]] = column[Option[Double]]("width", O.Default(None))
    /** Database column height SqlType(float8), Default(None) */
    val height: Rep[Option[Double]] = column[Option[Double]]("height", O.Default(None))
  }
  /** Collection-like TableQuery object for table tGroups */
  lazy val tGroups = new TableQuery(tag => new tGroups(tag))

  /** Entity class storing rows of table tStaffMac
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param mac Database column mac SqlType(varchar), Length(255,true)
    *  @param groupId Database column group_id SqlType(int8)
    *  @param userId Database column user_id SqlType(int8) */
  case class rStaffMac(id: Long, mac: String, groupId: Long, userId: Long)
  /** GetResult implicit for fetching rStaffMac objects using plain SQL queries */
  implicit def GetResultrStaffMac(implicit e0: GR[Long], e1: GR[String]): GR[rStaffMac] = GR{
    prs => import prs._
      rStaffMac.tupled((<<[Long], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table staff_mac. Objects of this class serve as prototypes for rows in queries. */
  class tStaffMac(_tableTag: Tag) extends profile.api.Table[rStaffMac](_tableTag, "staff_mac") {
    def * = (id, mac, groupId, userId) <> (rStaffMac.tupled, rStaffMac.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(mac), Rep.Some(groupId), Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> rStaffMac.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column mac SqlType(varchar), Length(255,true) */
    val mac: Rep[String] = column[String]("mac", O.Length(255,varying=true))
    /** Database column group_id SqlType(int8) */
    val groupId: Rep[Long] = column[Long]("group_id")
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
  }
  /** Collection-like TableQuery object for table tStaffMac */
  lazy val tStaffMac = new TableQuery(tag => new tStaffMac(tag))

  /** Entity class storing rows of table tUserAction
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param cilentMac Database column cilent_mac SqlType(varchar), Length(255,true)
    *  @param groupId Database column group_id SqlType(int8)
    *  @param inTime Database column in_time SqlType(int8)
    *  @param outTime Database column out_time SqlType(int8), Default(None) */
  case class rUserAction(id: Long, cilentMac: String, groupId: Long, inTime: Long, outTime: Option[Long] = None)
  /** GetResult implicit for fetching rUserAction objects using plain SQL queries */
  implicit def GetResultrUserAction(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]]): GR[rUserAction] = GR{
    prs => import prs._
      rUserAction.tupled((<<[Long], <<[String], <<[Long], <<[Long], <<?[Long]))
  }
  /** Table description of table user_action. Objects of this class serve as prototypes for rows in queries. */
  class tUserAction(_tableTag: Tag) extends profile.api.Table[rUserAction](_tableTag, "user_action") {
    def * = (id, cilentMac, groupId, inTime, outTime) <> (rUserAction.tupled, rUserAction.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(cilentMac), Rep.Some(groupId), Rep.Some(inTime), outTime).shaped.<>({r=>import r._; _1.map(_=> rUserAction.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column cilent_mac SqlType(varchar), Length(255,true) */
    val cilentMac: Rep[String] = column[String]("cilent_mac", O.Length(255,varying=true))
    /** Database column group_id SqlType(int8) */
    val groupId: Rep[Long] = column[Long]("group_id")
    /** Database column in_time SqlType(int8) */
    val inTime: Rep[Long] = column[Long]("in_time")
    /** Database column out_time SqlType(int8), Default(None) */
    val outTime: Rep[Option[Long]] = column[Option[Long]]("out_time", O.Default(None))
  }
  /** Collection-like TableQuery object for table tUserAction */
  lazy val tUserAction = new TableQuery(tag => new tUserAction(tag))

  /** Entity class storing rows of table tUsers
    *  @param userId Database column user_id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param userName Database column user_name SqlType(varchar), Length(255,true)
    *  @param createTime Database column create_time SqlType(int8)
    *  @param loginPsw Database column login_psw SqlType(varchar), Length(255,true) */
  case class rUsers(userId: Long, userName: String, createTime: Long, loginPsw: String)
  /** GetResult implicit for fetching rUsers objects using plain SQL queries */
  implicit def GetResultrUsers(implicit e0: GR[Long], e1: GR[String]): GR[rUsers] = GR{
    prs => import prs._
      rUsers.tupled((<<[Long], <<[String], <<[Long], <<[String]))
  }
  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class tUsers(_tableTag: Tag) extends profile.api.Table[rUsers](_tableTag, "users") {
    def * = (userId, userName, createTime, loginPsw) <> (rUsers.tupled, rUsers.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(userName), Rep.Some(createTime), Rep.Some(loginPsw)).shaped.<>({r=>import r._; _1.map(_=> rUsers.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(bigserial), AutoInc, PrimaryKey */
    val userId: Rep[Long] = column[Long]("user_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_name SqlType(varchar), Length(255,true) */
    val userName: Rep[String] = column[String]("user_name", O.Length(255,varying=true))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
    /** Database column login_psw SqlType(varchar), Length(255,true) */
    val loginPsw: Rep[String] = column[String]("login_psw", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table tUsers */
  lazy val tUsers = new TableQuery(tag => new tUsers(tag))
}
