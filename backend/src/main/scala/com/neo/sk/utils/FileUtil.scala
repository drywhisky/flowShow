package com.neo.sk.utils

import java.io.{File, PrintWriter}
import org.slf4j.LoggerFactory
import scala.io.Source
import com.neo.sk.flowShow.common.AppSettings

/**
  * Created by liuziwei on 2016/7/10.
  */
object FileUtil {
  private[this] val log = LoggerFactory.getLogger(this.getClass)

  val targetDir = new File(AppSettings.tempPath)
  if(!targetDir.exists){
    targetDir.mkdirs()
    targetDir.mkdirs()
  }

  def saveDuration(fileName: String,gId:String, durationCache: Map[String, List[(Long, Long)]]) = {
    try {
      val durationFile = new File(AppSettings.tempPath + fileName)
      if (durationFile.exists()) durationFile.delete()
      val writer = new PrintWriter(durationFile)
      for (r <- durationCache) {
        val clientMac = r._1
        val durationList = r._2
        writer.write(s"$gId,$clientMac#${durationList.map(t => t._1 + ":" + t._2).mkString(",")}")
        writer.write("\n")
      }
      writer.close()
    }catch{
      case e:Exception =>
        log.error(s"save file $fileName error",e)
    }

    log.debug(s"SAVETEMPFILE TASK: ${durationCache.size} lines written into $fileName **********")
  }

  def readDuration(fileName: String) = {
//    val durationCache = mutable.HashMap[String, List[(Long, Long)]]()

    val durationFile = new File(AppSettings.tempPath + fileName)
    val cache = if(durationFile.exists()){
      val source = Source.fromFile(durationFile,"UTF-8")
      val cache = source.getLines().map{ line =>
        val (gId, clientMac) = (
          line.split("#")(0).split(",")(0),
          line.split("#")(0).split(",")(1)
          )
        val durationList = line.split("#")(1).split(",").map{ s =>
          (s.split(":")(0).toLong, s.split(":")(1).toLong)
        }.toList
        (clientMac, durationList)
      }.toMap
      source.close()
      cache
    }else {
      Map[String,List[(Long,Long)]]()
    }
    log.debug(s"read from file $fileName lines ${cache.size} *******************")
    cache
  }

}
