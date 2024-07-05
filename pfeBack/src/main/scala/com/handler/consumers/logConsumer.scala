package com.handler.consumers

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer,ConsumerRecords}
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.{Collections, Properties}

import akka.actor.typed.ActorRef
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.Right
import com.handler.model.{Command, MyEntity, updateNtt}
import com.handler.model.MyEntity._
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.handler.actor.entityHandler

object logConsumer {
  private val props = new Properties()
  props.put("bootstrap.servers", "my-cluster-kafka-bootstrap.kafka.svc.cluster.local:9092")
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "log")
  props.put(
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer"
  )
  props.put(
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringDeserializer"
  )
  props.put("auto.offset.reset", "earliest")
  def FetchCommands() = {

    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(Collections.singletonList("back_logs"))
    val simpleLogger = Behaviors.receive[Any] { (ctx, message) =>
      ctx.log.info(s"[logger] $message")
      Behaviors.same
    }

    // Poll for messages
    val root = Behaviors.setup[String] { ctx =>
      val logger = ctx.spawn(simpleLogger, "logger")
      try {
        while (true) {
          val records: ConsumerRecords[String, String] = consumer.poll(1000) // Poll timeout in milliseconds
          records.forEach { record =>
            println(record)
            val jsonString = record.value()
            val pNtt = MyEntity.createNtt(jsonString)
            pNtt match {
              case Right(ntt) =>
                val handlerId = s"${ntt.tenant_id}-${ntt.interId}-${ntt.p1}-${ntt.c1}"
                val nttHandler: ActorRef[Command] = ctx.child(handlerId).map(_.asInstanceOf[ActorRef[Command]]).getOrElse(ctx.spawn(entityHandler(ntt.tenant_id, ntt.interId, ntt.p1, ntt.c1), handlerId))
                nttHandler ! updateNtt(pNtt, logger)
                nttHandler


              case Left(error) =>
                // Handle the error
                println(s"Error creating entity from JSON string $jsonString: $error")
                // Return a default behavior or raise an exception as per your requirement
                Behaviors.same
            }
          }
        }

      } finally {
        consumer.close()
      }
      Behaviors.empty
    }
    val system = ActorSystem(root, "saving_promos")
    import system.executionContext
    system.scheduler.scheduleOnce(30.seconds, () => system.terminate())
    StdIn.readLine()
    println("consumer Out")
  }
}