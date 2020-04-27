package org.sunbird.dp.usercache.domain

import java.util

import org.sunbird.dp.core.domain.Events

class Event(eventMap: util.Map[String, Any]) extends Events(eventMap) {

  override def kafkaKey(): String = {
    did()
  }

  def getId: String = {
    Option(objectType()).map({ t => if (t.equalsIgnoreCase("User")) objectID() else null
    }).getOrElse(null)
  }

  def getState: String = {
    telemetry.read[String]("edata.state").getOrElse(null)
  }

  def getContextDataId(cDataType: String): String = {
    val cdata = telemetry.read[util.ArrayList[util.Map[String, AnyRef]]]("context.cdata").getOrElse(null)
    var signInType: String = null
    if (null != cdata && !cdata.isEmpty) {
      cdata.forEach(cdataMap => {
        if (cdataMap.get("type").asInstanceOf[String].equalsIgnoreCase(cDataType)) signInType = cdataMap.get("id").toString else signInType
      })
    }
    signInType
  }

  def userMetaData():util.ArrayList[String] = {
    telemetry.read[util.ArrayList[String]]("edata.props").getOrElse(new util.ArrayList[String]())
  }

}
