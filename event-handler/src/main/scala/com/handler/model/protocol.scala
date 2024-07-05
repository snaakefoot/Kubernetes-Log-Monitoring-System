package com.handler.model

import akka.actor.typed.ActorRef
import com.handler.model.MyEntity

import java.sql.Date

// commands
sealed trait Command
case class updateNtt(logOutput:Either[String, MyEntity], replyTo: ActorRef[nttProtocol]) extends Command


// events
sealed trait Event
case class nttUpdated(fieldsToUpdate:MyEntity) extends Event with nttProtocol
case class serializedNtt(nttString:String) extends  Event with nttProtocol

// communication with the "agent"
case class CommandFailure(reason: String) extends nttProtocol


trait nttProtocol
case class ManageNtt(ntt: ActorRef[Command]) extends nttProtocol
case class Generate(nCommands: Int) extends nttProtocol