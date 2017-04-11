package com.neo.sk.flowShow.common

import scala.collection.mutable
import com.neo.sk.flowShow.ptcl._

/**
  * Created by dry on 2017/4/11.
  */

object CacheMap {
  val residentList = mutable.HashMap[Int, List[DurationData]]()//(groupId,驻留时长统计）
  val brandList = mutable.HashMap[Int, List[BrandsData]]()//(groupId,品牌统计)
  val frequencyList = mutable.HashMap[Int, List[FrequencyData]]()//(groupId,到访频次统计)
  val ratioList = mutable.HashMap[Int, List[RatioData]]()//(groupId,老用户占比统计)

}

object realTimeCustomerCache{
  var totalPerson = mutable.HashMap[Int,Int]()   // 所有的数据用map是为了存每个group的数据
  var maxNum = mutable.HashMap[Int,Int]()
  val maxTime = mutable.HashMap[Int,Long]()

  val flowMap = mutable.HashMap[Int,List[(Int, Long)]]()
}
