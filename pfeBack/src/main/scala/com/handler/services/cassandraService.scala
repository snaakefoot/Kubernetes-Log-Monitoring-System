package com.handler.services

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.datastax.oss.driver.api.core.cql.SimpleStatement
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import java.net.InetSocketAddress
import scala.concurrent.{Await, Future}
import scala.util.{Properties, Try}
trait CassandraServiceI {
  def getPassword(email:String):String
  def executeQuery(query: String): ResultSet
  def updateHistory(tenant:String , persistentId: String ,updateDate:String):Boolean
  def addUser(email:String ,password: String):ResultSet
  def initialize(): Unit
  def checkAccount(email : String):Boolean
}

object CassandraService extends CassandraServiceI {

  val cassandraEndpoint =Properties.envOrElse("CASSANDRA_ENDPOINT", "localhost")
  val session: CqlSession = CqlSession.builder()
    .addContactPoint(new InetSocketAddress(cassandraEndpoint, 9042))
    .withLocalDatacenter("datacenter1")
    .build()

  override def getPassword(email:String) = {

    val res=session.execute(s"select * from accounts.users where email='${email}' ;")
    val hasRows = res.iterator().hasNext
    if(hasRows)
      res.one().getString("password")
    else
      "*"
  }
  override def updateHistory(tenant:String , persistentId: String ,updateDate:String):Boolean={
    val res = session.execute(s"select * from history.history where tenant='${tenant}' and  persistentId='${persistentId}' ;")
    val hasRows = res.iterator().hasNext
    if(hasRows){
      val nb=res.one().getInt("numberofupdates")
      val res2=Future {session.execute(s"UPDATE history.history  SET numberofupdates = ${nb+1}, lastupdatedate = '${updateDate}' where tenant='${tenant}' and  persistentId='${persistentId}';")}
        Await.result(
          res2,
          Duration.Inf
        ).wasApplied()
    }
    else{
      val res4=Future {session.execute(s"INSERT INTO history.history (tenant,persistentId,lastupdatedate,numberofupdates)  VALUES( '${tenant}','${persistentId}','${updateDate}',1)")}
      Await.result(
        res4,
        Duration.Inf
      ).wasApplied()
    }
  }
  override def executeQuery(query: String): ResultSet = {
    val statement = SimpleStatement.builder(query)
      // Set the fetch size
      .setPageSize(100000000) // Set your desired fetch size
      .build()
    val resultSet = session.execute(statement) // Execute the query
    // Process the result set, print for example

    resultSet
  }
  override def addUser(email: String, password: String): ResultSet ={

    val resultSet =session.execute(s"INSERT INTO accounts.users (email,id,password)  VALUES( '${email}',uuid(),'${password}')")

    resultSet
  }

  override  def initialize(): Unit = {
      try {
        // Create namespace (keyspace) named "accounts"
        val createNamespaceCql1 = "CREATE KEYSPACE IF NOT EXISTS accounts WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"
        val createNamespaceCql2 = "CREATE KEYSPACE IF NOT EXISTS history WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};"
        session.execute(createNamespaceCql1)
        session.execute(createNamespaceCql2)
        val createTableCql1 = "CREATE TABLE IF NOT EXISTS accounts.Users (Email text,id uuid,password text,PRIMARY KEY (email, id));"
        val createTableCql2 = "CREATE TABLE IF NOT EXISTS history.history ( tenant text ,persistentId text,lastUpdateDate text,NumberOfUpdates int,PRIMARY KEY (tenant ,persistentId));"
        session.execute(createTableCql1)
        session.execute(createTableCql2)
        println("Namespace and table created successfully.")
      }
    }
  override  def checkAccount(email : String):Boolean={

    val res=session.execute(s"select * from accounts.users where email='${email}' ;")
    val hasRows = res.iterator().hasNext

    hasRows
  }
}