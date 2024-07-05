package com.handler.api

import akka.http.scaladsl.Http
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.handler.services.websocket._
import com.handler.model.{MyEntity, Myrequest, tenantRow}
import io.circe._
import io.circe.syntax._
import io.circe.parser._

import scala.io.StdIn
import spray.json._
import com.handler.services.{CassandraService, loginService, signinSerivice, tenantService}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.handler.services.tenantService.getNumberOfEvents
import com.handler.consumers.logConsumer._
import com.handler.model
import io.circe.Json
import io.circe.generic.semiauto.deriveEncoder

import scala.concurrent.duration.DurationInt
trait MyJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val request = jsonFormat2(Myrequest)
  implicit val MytenantRow = jsonFormat6(tenantRow)
}
object MyRoutes extends MyJsonProtocol {

  val ServerRoutes: Route = cors() {

    def authenticate(route: => Route): Route = {
      parameter("token".?) {
        case Some(token) =>
          if (loginService.isTokenValid(token)) {
            if (loginService.isTokenExpired(token)) {
              println("expired")
              complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "Token expired."))
            } else {
              route // Execute the route inside the authenticated block
            }
          } else {
            println("invalid")
            complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "Token is invalid, or has been tampered with."))
          }
        case None =>
          println("no token")
          complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "No token provided!"))
      }
    }
    val getFieldsStates: Route =
      path("allStatesFields") {
        get {
          parameterMultiMap { params =>
            val id = params.getOrElse("id",List()).head
            val paramValues = params.getOrElse("param", List())
            println(paramValues,id)
            complete(fieldsStatesWebSocketFlow(id,paramValues))
          }
        }
      }
    val getFieldsEvents: Route =
      path("allEventsFields") {
        get {
          parameterMultiMap { params =>
            val id = params.getOrElse("id",List()).head
            val paramValues = params.getOrElse("param", List())
            println(paramValues,id)
            complete(fieldsEventsWebSocketFlow(id,paramValues))
          }
        }
      }
        val signinRoute: Route =path("signin") {
      post {
        entity(as[Myrequest]) { Request =>
          val email = Request.email
          val password = Request.password
          signinSerivice.signin(email, password)
        }
      }
    }
    val loginRoute: Route =path("login") {
      post {
        entity(as[Myrequest]) { Request =>
          val email = Request.email
          val password = Request.password
          if (loginService.checkPassword(email, password)) {
            complete(loginService.createToken(email, 1))
          } else
            complete(StatusCodes.Unauthorized)
        }
      }
    }
    val IdsRoute: Route =path("getIds") {
      get {
        complete(getPersistenceIds().toJson)
      }
    }
    val nbrOfEventsRoute: Route =path("getNumberOfEvents") {
      get {
        parameters("persistentId"){(persistentId)=>complete(getNumberOfEvents(persistentId).toJson)}
      }
    }
    val eventRoute =
      path("events" / Segment/ Segment/ Segment) { (persistenceId,skip,numberOf )=>
        authenticate(handleWebSocketMessages(eventsWebSocketFlow(persistenceId,skip.toInt,numberOf.toInt).idleTimeout(180.seconds)))
      }
    val allEventRoute =
      path("allEvents" / Segment /  Segment/ Segment ) { (persistenceId,skip,numberOf)=>

        authenticate(handleWebSocketMessages(allEventsWebSocketFlow(persistenceId)))
      }
    val stateRoute = {
      path("states" / Segment/ Segment/ Segment) { (persistenceId,skip,numberOf )=>
        authenticate(handleWebSocketMessages(StatesWebSocketFlow(persistenceId,skip.toInt,numberOf.toInt)))
      }
    }
    val allStatesRoute =
      path("allStates" / Segment / Segment/ Segment) { (persistenceId,skip,numberOf)=>
        authenticate(handleWebSocketMessages(allStatesWebSocketFlow(persistenceId)))
      }
    val TenantRoute =
      (path("tenant") & get)  {
        optionalHeaderValueByName("Authorization") {
          case Some(token) =>
            if (loginService.isTokenValid(token)) {
              if (loginService.isTokenExpired(token)) {
                complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "Token expired."))
              } else {
                parameters("tenant"){(tenant)=>complete(tenantService.getEntities(tenant))}
              }
            } else {
              complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "Token is invalid, or has been tampered with."))
            }
          case _ => complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "No token provided!"))
        }
      }
    val home =
      path("/") {
          get {
          complete("ITS ALIVE !")
        }
      }
    home ~ signinRoute ~ loginRoute ~ IdsRoute ~ allEventRoute ~ eventRoute ~ allStatesRoute ~ stateRoute ~ TenantRoute ~ nbrOfEventsRoute ~ getFieldsEvents ~ getFieldsStates
  }
}


object routes extends App {
  implicit val system = ActorSystem("WebSocketSystem")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(2.seconds)

  CassandraService.initialize()
  // all events for a persistence ID

  Http().newServerAt("0.0.0.0", 8080).bind(MyRoutes.ServerRoutes)
  println("Server started at http://localhost:8080/")
  //FetchCommands()
  StdIn.readLine()
  println("Server is shut down")
}