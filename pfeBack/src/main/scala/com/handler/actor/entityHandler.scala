package com.handler.actor

import akka.actor.typed.ActorRef

import scala.util.Right

import com.handler.model._
import com.handler.services.CassandraService._
import akka.actor.typed.Behavior
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior, RetentionCriteria}
import io.circe.Json
import org.slf4j.LoggerFactory

import scala.math.Integral.Implicits.infixIntegralOps
object entityHandler {
  val logger = LoggerFactory.getLogger(getClass)

  def commandHandler(tenant_id:String,interId: String, p1: String, c1: String): (MyEntity, Command) => Effect[Event, MyEntity] = (oldNtt, command) =>

    command match {
      case updateNtt(logOutput,replyTo) => logOutput match {

        case Right(newNtt) =>
          val updatedFields=MyEntity.createNttEvent(Some(oldNtt).toRight("ntt to either transformation failed"),Some(newNtt).toRight("ntt to either transformation failed"))
          updatedFields match {
            case Right(ntt) => {
              updateHistory(tenant_id,s"$tenant_id-$interId-$p1-$c1",ntt.timestamp)
              Effect.persist(nttUpdated(ntt)).thenReply(replyTo)(_ => nttUpdated(ntt))
            }
            case Left(error) => Effect.reply(replyTo)(CommandFailure(error))
          }
        case Left(error) => Effect.reply(replyTo)(CommandFailure(error))
      }

    }

  def eventHandler(tenant_id:String,interId: String, p1: String, c1: String): (MyEntity, Event) => MyEntity = (state, event) =>
    event match {
      case nttUpdated(fieldsToUpdate) =>
        val newState = MyEntity.updateNtt(Some(state).toRight("ntt to either transformation failed"),Some(fieldsToUpdate).toRight("ntt to either transformation failed"))
        logger.info(s"State changed: $newState")

        newState.getOrElse(state)
    }

  def apply(tenant_id:String,interId: String, p1: String, c1: String): Behavior[Command] = {
    val nttUniqueId=s"$tenant_id-$interId-$p1-$c1"
    EventSourcedBehavior[Command, Event, MyEntity](
      persistenceId = PersistenceId.ofUniqueId(nttUniqueId),
      emptyState = MyEntity(tenant_id,interId, p1, c1, "10-04-03 12:17:44.751200", Json.obj(), Json.obj()),
      commandHandler = commandHandler(tenant_id,interId, p1, c1),
      eventHandler = eventHandler(tenant_id,interId, p1, c1),
    ).withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 3, keepNSnapshots = 2))
  }
}