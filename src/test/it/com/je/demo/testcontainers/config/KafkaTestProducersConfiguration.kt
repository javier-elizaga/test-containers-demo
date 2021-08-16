package com.je.demo.testcontainers.config

import com.je.demo.testcontainers.application.todo.kafka.config.KafkaApplicationProperties
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.KafkaContainer

@TestConfiguration
@Profile("kafka-test-producers")
class KafkaTestProducersConfiguration(
    private val beanFactory: ConfigurableListableBeanFactory,
    private val kafkaApplicationProperties: KafkaApplicationProperties,
    private val kafka: KafkaContainer,
    private var schemaRegistry: SchemaRegistryContainer
) {
    private val log = LoggerFactory.getLogger(KafkaTestProducersConfiguration::class.java)

    init {
        createProducers()
    }

    private val topicNames get() = kafkaApplicationProperties.topicNames

    /**
     * Creates and register as beans Kafka producer for all topic in topicNames.
     */
    private fun createProducers() {
        log.info("Creating Kafka Producers")
        topicNames
            .map { topicName ->
                val producer = producer()
                val qualifier = "$topicName-producer"
                beanFactory.initializeBean(producer, qualifier)
                beanFactory.autowireBean(producer)
                beanFactory.registerSingleton(qualifier, producer)
                log.info("Kafka Producer Created with qualifier: $qualifier")
                producer
            }
    }

    private fun producer(): KafkaProducer<String, Any> {
        val configs = HashMap<String, Any>()
        configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
        configs[AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistry.url
        configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java
        return KafkaProducer<String, Any>(configs)
    }
}
