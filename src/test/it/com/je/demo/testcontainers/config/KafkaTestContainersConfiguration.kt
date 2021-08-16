package com.je.demo.testcontainers.config

import com.je.demo.testcontainers.application.todo.kafka.config.KafkaApplicationProperties
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.utility.DockerImageName
import java.util.Properties
import javax.annotation.PreDestroy

@TestConfiguration
@Profile("kafka")
class KafkaTestContainersConfiguration(
    @Value("\${application.test-containers.kafka-image}") kafkaImage: String,
    @Value("\${application.test-containers.schema-registry-image}") schemaRegistryImage: String,
    private val beanFactory: ConfigurableListableBeanFactory,
    private val kafkaApplicationProperties: KafkaApplicationProperties,
) {
    private val log = LoggerFactory.getLogger(KafkaTestContainersConfiguration::class.java)
    private var schemaRegistry: SchemaRegistryContainer
    private var kafka: KafkaContainer

    init {
        val network = Network.newNetwork()
        kafka = KafkaContainer(DockerImageName.parse(kafkaImage)).withNetwork(network)
        kafka.start()
        schemaRegistry = SchemaRegistryContainer(
            schemaRegistryImage,
            "PLAINTEXT://${kafka.networkAliases[0]}:9092"
        ).withNetwork(network)
        schemaRegistry.start()
        createTopics()
    }

    @PreDestroy
    fun clean() {
        schemaRegistry.stop()
        kafka.stop()
    }

    @Bean
    fun kafkaContainer() = kafka

    @Bean
    fun schemaRegistryContainer() = schemaRegistry

    @Primary
    @Bean
    fun kafkaProperties(): KafkaProperties {
        val kafkaProperties = KafkaProperties()
        kafkaProperties.bootstrapServers = listOf(kafka.bootstrapServers)
        val defaultProps = kafkaProperties.properties
        defaultProps[AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistry.url
        return kafkaProperties
    }

    private val topicNames get() = kafkaApplicationProperties.topicNames

    private fun createAdminClient(): AdminClient {
        val properties = Properties()
        properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafka.bootstrapServers
        return KafkaAdminClient.create(properties)
    }

    private fun createTopics() {
        createAdminClient().use { adminClient ->
            val replication: Short = 1
            val partitions = 1
            val topics = topicNames.map { n -> NewTopic(n, partitions, replication) }
            adminClient.createTopics(topics)
        }
    }
}
