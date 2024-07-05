package com.handler.services

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.StandardRoute
import org.mindrot.jbcrypt.BCrypt
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import com.handler.model.tenantRow

trait tenantServiceI {

  def getEntities(tenantId:String):List[tenantRow]
  def getNumberOfEvents(persistentid: String): String
}

object tenantService extends tenantServiceI {
  override def getEntities(tenantId:String):List[tenantRow] = {
    val q = s"SELECT * FROM history.history WHERE tenant='$tenantId';"
    val ResultSet = CassandraService.executeQuery(q);
    var res:List[tenantRow]= List[tenantRow]()
    val ans=ResultSet.forEach(row=> {
      val Ids=row.getString("persistentid").split("-").toList
      res = res :+
      tenantRow(
        row.getString("tenant"),
        Ids.lift(1).getOrElse("None"),
        Ids.lift(2).getOrElse("None"),
        Ids.lift(3).getOrElse("None"),
        row.getString("lastupdatedate"),
        row.getInt("numberofupdates")
      )
    }
    )
    res
  }
  override def getNumberOfEvents(persistentid: String): String = {
    val Ids=persistentid.split("-").toList
    val q = s"SELECT * FROM history.history WHERE tenant='${Ids(0)}' and persistentid='$persistentid';"
    val res = CassandraService.executeQuery(q);
    val hasRows = res.iterator().hasNext
    if(hasRows)
      res.one().getInt("numberofupdates").toString
    else
      "-1"
  }
}