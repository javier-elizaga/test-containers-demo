package com.je.demo.testcontainers.application.todo.kafka.config

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.ErrorHandler
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class KafkaErrorHandler : ErrorHandler {

    private val log = LoggerFactory.getLogger(KafkaErrorHandler::class.java)

    override fun handle(ex: Exception, record: ConsumerRecord<*, *>?) {
        val topic = record?.topic() ?: "not-defined"
        log.error("Kafka error consuming message from: $topic", ex)
    }
}
