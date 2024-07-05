package com.handler.model

import java.sql.Date
import scala.util.Random
import io.circe.Json
import io.circe._

import io.circe.generic.semiauto._
import io.circe.parser._

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneId}

case class MyEntity (
                        tenant_id: String,
                        interId: String,
                        p1: String,
                        c1: String,
                        timestamp: String,
                        metrics: Json,
                        k8sData: Json,
                      )
object MyEntity {


  def createNtt(jsonString: String): Either[String, MyEntity] = {
    parse(jsonString).flatMap { json =>{

      for {
        messageJson <- json.hcursor.get[Json]("message").toOption.toRight("Failed to get 'message' as JSON")

        kubernetes <- json.hcursor.get[Json]("kubernetes").toOption.toRight("Failed to get 'kubernetes'")
        timestamp <- json.hcursor.get[Double]("@timestamp").toOption.toRight("Failed to get '@timestamp'")

        cleanedMessage = messageJson.noSpaces.substring(105, messageJson.noSpaces.length - 1).replace("\\", "")
        messageJsonParsed <- parse(cleanedMessage).toOption.toRight("Failed to parse cleaned message")
        seconds = timestamp.toLong
        nanoseconds = ((timestamp % 1) * 1e9).toLong

        dateTime = Instant.ofEpochSecond(seconds, nanoseconds)
        formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss.SSSSSS")
        formattedDateTime = LocalDateTime.ofInstant(dateTime, ZoneId.systemDefault()).format(formatter)
        tenant_id <- messageJsonParsed.hcursor.get[String]("tenantId").toOption.toRight("Failed to get 'tenant_id'")
        interId <- messageJsonParsed.hcursor.get[String]("interId").toOption.toRight("Failed to get 'interId'")
        p1 <- messageJsonParsed.hcursor.get[String]("p1").toOption.toRight("Failed to get 'p1'")
        c1 <- messageJsonParsed.hcursor.get[String]("c1").toOption.toRight("Failed to get 'c1'")

      } yield MyEntity(tenant_id,interId, p1, c1, formattedDateTime, messageJsonParsed, kubernetes)}
    }.left.map(err => s"Parsing error: $err")
  }

  def updateNtt(oldNtt:Either[String, MyEntity],newNtt:Either[String, MyEntity]): Either[String, MyEntity] = {
    (oldNtt,newNtt) match {
      case (Right(oldNtt),Right(newNtt)) =>  Right(MyEntity(newNtt.tenant_id,newNtt.interId,newNtt.p1,newNtt.c1,newNtt.timestamp,oldNtt.metrics.deepMerge(newNtt.metrics),oldNtt.k8sData.deepMerge(newNtt.k8sData)))
      case _ => Left("Merge Failed due to invalid entities")
    }
  }

  def serializedEvent(ntt:MyEntity): String = {
    Json.obj(
      ("tenant_id", Json.fromString(ntt.tenant_id)),
      ("interId", Json.fromString(ntt.interId)),
      ("p1", Json.fromString(ntt.p1)),
      ("c1", Json.fromString(ntt.c1)),
      ("timestamp", Json.fromString(ntt.timestamp)),
      ("metrics", ntt.metrics),
      ("k8sData", ntt.k8sData)
    ).noSpaces
  }

  def createNttEvent(oldNtt:Either[String, MyEntity],newNtt:Either[String, MyEntity]): Either[String, MyEntity]={
    (oldNtt,newNtt) match {
      case (Right(oldNtt),Right(newNtt)) => {
        val updatedMetric=(oldNtt.metrics.asObject, newNtt.metrics.asObject) match {
          case (Some(obj1), Some(obj2)) =>
            val diff = obj2.toIterable.foldLeft(JsonObject.empty) { case (accumulator, (key, value2)) =>
              obj1(key) match {
                case Some(value1) if value1 == value2 => accumulator
                case _ => accumulator.add(key, value2)
              }
            }
            Json.fromJsonObject(diff)
          case _ => Json.Null  // Fallback if either j1 or j2 is not a JSON object
        }
//        val updatedK8s=(oldNtt.k8sData.asObject, newNtt.k8sData.asObject) match {
//          case (Some(obj1), Some(obj2)) =>
//            val diff = obj2.toIterable.foldLeft(JsonObject.empty) { case (accumulator, (key, value2)) =>
//              obj1(key) match {
//                case Some(value1) if value1 == value2 => accumulator
//                case _ => accumulator.add(key, value2)
//              }
//            }
//            Json.fromJsonObject(diff)
//          case _ => Json.Null
//        }
        Right(MyEntity(newNtt.tenant_id,newNtt.interId,newNtt.p1,newNtt.c1,newNtt.timestamp,updatedMetric,newNtt.k8sData))
      }
      case _ => Left("Merge Failed due to invalid entities")
    }
  }

}