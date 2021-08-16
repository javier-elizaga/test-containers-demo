package com.je.demo.testcontainers.application.todo.kafka

import com.je.demo.testcontainers.application.todo.kafka.config.CreateTodoContainerFactory
import com.je.demo.testcontainers.avro.CreateTodoEvent
import com.je.demo.testcontainers.config.TestContainersTest
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.usecase.CreateTodoUseCase
import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Import
import org.springframework.core.convert.converter.Converter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@TestContainersTest
@Import(value = [LatchedCreateTodoKafkaConsumerConfiguration::class])
internal class CreateTodoKafkaConsumerConfigurationIT {

    private val log = LoggerFactory.getLogger(CreateTodoKafkaConsumerConfigurationIT::class.java)

    @Autowired
    lateinit var context: ApplicationContext

    @Autowired
    lateinit var latchedConsumer: LatchedCreateTodoKafkaConsumerConfiguration

    @Autowired
    lateinit var repo: JpaRepository<TodoEntity, Long>

    @Value("\${application.kafka.create-todo.topic}")
    lateinit var topicName: String

    lateinit var producer: KafkaProducer<String, Any>

    @BeforeEach
    @Suppress("UNCHECKED_CAST")
    internal fun setUp() {
        producer = context.getBean("$topicName-producer") as KafkaProducer<String, Any>
    }

    @Test
    internal fun `when createTodoEvent is received a Todo is created in db`() {
        val recordCountBeforeTest = repo.count()
        log.debug("Number of records before test: {}", recordCountBeforeTest)

        latchedConsumer.resetLatch()
        val event = CreateTodoEvent("description")
        val record = ProducerRecord<String, Any>(
            topicName,
            "35ed4fe4-5cda-4e53-abe3-bfe3fea6396d",
            event
        )
        if (!sendAndWaitLatch(record)) return fail()
        val recordCountAfterTest = repo.count()
        log.debug("Number of records after test: {}", recordCountAfterTest)
        assertEquals(recordCountBeforeTest + 1, recordCountAfterTest)
    }

    private fun sendAndWaitLatch(record: ProducerRecord<String, Any>): Boolean {
        producer.send(record).get()
        log.info("Waiting latch to be released")

        val maxRetries = 3
        var retry = 0
        var isReleased: Boolean
        do {
            isReleased = latchedConsumer.await()
            log.info("Latch realised: {}", isReleased)
        } while (latchedConsumer.count() == 1L || retry++ > maxRetries)
        return isReleased
    }
}

@TestConfiguration
@ActiveProfiles(profiles = ["kafka"])
class LatchedCreateTodoKafkaConsumerConfiguration(
    createTodoUseCase: CreateTodoUseCase,
    eventToDto: Converter<CreateTodoEvent, CreateTodoDto>
) : CreateTodoKafkaConsumerConfiguration(createTodoUseCase, eventToDto) {

    private var latch: CountDownLatch? = null

    @KafkaListener(
        topics = ["\${application.kafka.create-todo.topic}"],
        containerFactory = CreateTodoContainerFactory,
        groupId = "\${application.kafka.create-todo.consumer-group-id}",
        autoStartup = "true"
    )
    override fun consumer(
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: String,
        @Payload event: CreateTodoEvent,
    ) {
        super.consumer(topic, key, event)
        latch!!.countDown()
    }

    fun resetLatch(): CountDownLatch {
        latch = CountDownLatch(1)
        return latch!!
    }

    fun await() = latch!!.await(10L, TimeUnit.SECONDS)

    fun count() = latch!!.count
}
