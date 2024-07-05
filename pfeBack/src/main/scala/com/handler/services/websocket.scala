package com.handler.services

import akka.NotUsed

import scala.util.matching.Regex
import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.handler.model.{MyEntity, nttUpdated}
import io.circe._
import io.circe.syntax._
import io.circe.parser._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer
import akka.stream.scaladsl.{Concat, Source}
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
trait websocketI {
  def eventsSource(persistenceId:String,skip:Int,numberOf:Int )(implicit system: ActorSystem, mat: Materializer): Source[EventEnvelope, _]
  def CurrenteventsSource(persistenceId:String,skip:Int,numberOf:Int )(implicit system: ActorSystem, mat: Materializer): Source[EventEnvelope, _]
  def getJsonEvent(ntt:MyEntity) : String
  def getLatestState(persistenceId:String,skip:Int) : MyEntity
  def StatesWebSocketFlow(persistenceId:String,skip:Int,numberOf:Int ): Flow[Message, Message, _]
  def eventsWebSocketFlow(persistenceId:String,skip:Int,numberOf:Int ): Flow[Message, Message, _]
  def getPersistenceIds(): Array[String]
  def allEventsWebSocketFlow(intersectionId:String ): Flow[Message, Message, _]
  def fieldsEventsWebSocketFlow(id: String, fields: List[String]): Array[String]
  def fieldsStatesWebSocketFlow(id: String, fields: List[String]): Array[String]
}


object websocket extends websocketI {
  implicit val system = ActorSystem("WebSocketSystem")
  implicit val materializer = ActorMaterializer()
  def getPersistenceIds(): Array[String] ={
    val readJournal = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
    val mutableArraySink: Sink[String, Future[ArrayBuffer[String]]] = Sink.fold(ArrayBuffer[String]()) { (array, element) =>
      array.append(element)
      array
    }
    val result: Future[ArrayBuffer[String]] = readJournal.currentPersistenceIds().runWith(mutableArraySink)
    Await.result(result, 10.seconds).toArray
  }
  def eventsSource(persistenceId:String,skip:Int,numberOf:Int )(implicit system: ActorSystem, mat: Materializer)= {

    val readJournal = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
    readJournal
      .eventsByPersistenceId(persistenceId, skip, numberOf)
  }
  def CurrenteventsSource(persistenceId:String,skip:Int,numberOf:Int )(implicit system: ActorSystem, mat: Materializer)= {

    val readJournal = PersistenceQuery(system).readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)
    readJournal
      .currentEventsByPersistenceId(persistenceId, skip, numberOf)
  }
  def getJsonEvent(ntt:MyEntity)= {
    Json.arr(Json.fromString(ntt.timestamp),ntt.metrics, ntt.k8sData,Json.fromString(ntt.interId), Json.fromString(ntt.p1), Json.fromString(ntt.c1)).noSpaces
  }
  def getLatestState(persistenceId:String,skip:Int) = {
    var state=MyEntity("0","0", "0", "0", "10-04-03 12:17:44.751200", Json.obj(), Json.obj())
    if(skip>0) {
      val outgoingMessages: Source[Unit, _] = {
        eventsSource(persistenceId, 0, skip )
          .map(_.event)
          .map {
            case nttUpdated(fieldsToUpdate) =>
              MyEntity.updateNtt(Some(state).toRight("ntt to either transformation failed"), Some(fieldsToUpdate).toRight("ntt to either transformation failed")) match {
                case Right(ntt) =>
                  println(ntt)
                  state = ntt
                case Left(err) => println(err)
              }
            case str: String => println(str)
          }
      }
      val streamCompletion: Future[_] = outgoingMessages.runWith(Sink.ignore)
      Await.result(streamCompletion, 10.seconds)
    }
    state
  }
  def StatesWebSocketFlow(persistenceId:String,skip:Int,numberOf:Int ) = {
    var state=getLatestState(persistenceId,skip)

    val incomingMessages: Sink[Message, Any] = Sink.ignore
    val outgoingMessages: Source[Message, _] = {
      eventsSource(persistenceId, skip+1, skip+numberOf)
        .map(_.event)
        .map {
          case nttUpdated(fieldsToUpdate) => {
            MyEntity.updateNtt(Some(state).toRight("ntt to either transformation failed"), Some(fieldsToUpdate).toRight("ntt to either transformation failed")) match {
              case Right(ntt) => {
                state = ntt

                TextMessage(getJsonEvent(state))
              }
              case Left(err) => TextMessage(err)
            }

          }
          case str: String => TextMessage(str)
        }
    }
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
  def eventsWebSocketFlow(persistenceId:String,skip:Int,numberOf:Int ) = {
    val incomingMessages: Sink[Message, Any] = Sink.ignore
    val outgoingMessages= eventsSource(persistenceId,skip+1,skip+numberOf )
      .map(_.event)
      .map{
        case nttUpdated(ntt) => TextMessage(getJsonEvent(ntt))
        case str:String => TextMessage(str)
      }

    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
  def allEventsWebSocketFlow(id:String ) = {
    val incomingMessages = Sink.ignore

    val sourcesArray= getPersistenceIds().filter(id.r.matches(_)).map(CurrenteventsSource(_, 0, Int.MaxValue)
      .map(_.event)
      .map{
        case nttUpdated(ntt) => TextMessage(getJsonEvent(ntt))
        case err:String => TextMessage(err)
      }
    )

    val outgoingMessages=sourcesArray.reduce((src1, src2) => Source.combine(src1, src2)(Concat(_)))
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
  def allStatesWebSocketFlow(id:String ) = {
    val incomingMessages = Sink.ignore
    val sourcesArray= getPersistenceIds().filter(id.r.matches(_)).map(id => {
      var state=MyEntity("0","0", "0", "0", "10-04-03 12:17:44.751200", Json.obj(), Json.obj())
      CurrenteventsSource(id, 0, Int.MaxValue)
        .map(_.event)
        .map {
          case nttUpdated(fieldsToUpdate) =>
            MyEntity.updateNtt(Some(state).toRight("ntt to either transformation failed"), Some(fieldsToUpdate).toRight("ntt to either transformation failed")) match {
              case Right(ntt) => {
                state = ntt

                TextMessage(getJsonEvent(state))
              }
              case Left(err) => TextMessage(err)
            }
          case str: String => TextMessage(str)
        }
    }
    )
    val outgoingMessages=sourcesArray.reduce((src1, src2) => Source.combine(src1, src2)(Concat(_)))
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }

  override def fieldsEventsWebSocketFlow(id: String, fields: List[String]): Array[String] = {
    getPersistenceIds().filter(id.r.matches(_)).flatMap(id => {
      val mutableArraySink: Sink[String, Future[ArrayBuffer[String]]] = Sink.fold(ArrayBuffer[String]()) { (array, element) =>
        array.append(element)
        array
      }
      val mySource = {
        CurrenteventsSource(id, 0, Int.MaxValue)
          .map(_.event)
          .map {
            case nttUpdated(fieldsToUpdate) => fieldsToUpdate
            case str: String => str
          }.filter {
            case (fieldsToUpdate: MyEntity) =>

              fields.exists(key => fieldsToUpdate.metrics.asObject.get(key).isDefined)
            case _ => false
          }.map {
            case (ntt: MyEntity) => {
              MyEntity.serializedEvent(ntt)
            }
          }
      }
      val result = mySource.runWith(mutableArraySink)
      Await.result(result, 10.seconds).toArray
    }
    )

  }

  override def fieldsStatesWebSocketFlow(id: String, fields: List[String]): Array[String] = {
    getPersistenceIds().filter(id.r.matches(_)).flatMap(id => {
      var state = MyEntity("0", "0", "0", "0", "10-04-03 12:17:44.751200", Json.obj(), Json.obj())

      val mutableArraySink: Sink[String, Future[ArrayBuffer[String]]] = Sink.fold(ArrayBuffer[String]()) { (array, element) =>
        array.append(element)
        array
      }
      val mySource = {
        CurrenteventsSource(id, 0, Int.MaxValue)
          .map(_.event)
          .map {
            case nttUpdated(fieldsToUpdate) =>
              MyEntity.updateNtt(Some(state).toRight("ntt to either transformation failed"), Some(fieldsToUpdate).toRight("ntt to either transformation failed")) match {
                case Right(ntt) => {
                  state = ntt
                  (ntt, fieldsToUpdate)
                }
                case Left(err) => println(err)
              }
            case str: String => str
          }.filter {
            case (ntt: MyEntity, fieldsToUpdate: MyEntity) =>

              fields.exists(key => fieldsToUpdate.metrics.asObject.get(key).isDefined)
            case _ => false
          }.map {
            case (ntt: MyEntity, fieldsToUpdate: MyEntity) => {
              MyEntity.serializedEvent(ntt)
            }
          }
      }
      val result = mySource.runWith(mutableArraySink)
      Await.result(result, 10.seconds).toArray
    }
    )

  }


}
