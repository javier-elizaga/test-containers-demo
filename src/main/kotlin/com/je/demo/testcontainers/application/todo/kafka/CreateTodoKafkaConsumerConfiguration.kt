package com.je.demo.testcontainers.application.todo.kafka

import com.je.demo.testcontainers.application.todo.kafka.config.CreateTodoContainerFactory
import com.je.demo.testcontainers.avro.CreateTodoEvent
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.usecase.CreateTodoUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload

@Configuration
class CreateTodoKafkaConsumerConfiguration(
    private val createTodoUseCase: CreateTodoUseCase,
    private val eventToDto: Converter<CreateTodoEvent, CreateTodoDto>
) {

    private val log = LoggerFactory.getLogger(CreateTodoKafkaConsumerConfiguration::class.java)

    @KafkaListener(
        topics = ["\${application.kafka.create-todo.topic}"],
        containerFactory = CreateTodoContainerFactory,
        groupId = "\${application.kafka.create-todo.consumer-group-id}",
        autoStartup = "\${application.kafka.create-todo.auto-start:true}"
    )
    fun consumer(
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: String,
        @Payload event: CreateTodoEvent,
    ) {
        log.debug("Received event from topic: {} and key: {}", topic, key)
        val dto = eventToDto.convert(event) ?: throw RuntimeException()
        log.debug("Event converted to dto: {}", dto)
        createTodoUseCase.createTodo(dto)
    }
}
