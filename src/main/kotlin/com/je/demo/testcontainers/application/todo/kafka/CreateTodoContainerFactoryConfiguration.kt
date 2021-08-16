package com.je.demo.testcontainers.application.todo.kafka

import com.je.demo.testcontainers.application.todo.kafka.config.CreateTodoContainerFactory
import com.je.demo.testcontainers.application.todo.kafka.config.KafkaApplicationProperties
import com.je.demo.testcontainers.application.todo.kafka.config.KafkaContainerFactoryBase
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.listener.ErrorHandler

@Configuration
class CreateTodoContainerFactoryConfiguration : KafkaContainerFactoryBase() {

    @Autowired
    lateinit var kafkaApplicationProperties: KafkaApplicationProperties

    private val consumerClientId get() = kafkaApplicationProperties.createTodo!!.consumerClientId

    @Bean(CreateTodoContainerFactory)
    fun containerFactoryBean(
        configurer: ConcurrentKafkaListenerContainerFactoryConfigurer,
        kafkaProperties: KafkaProperties,
        errorHandler: ErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<Any, Any> {
        return containerFactory(configurer, kafkaProperties, errorHandler)
    }

    override fun consumerConfig(properties: KafkaProperties): Map<String, Any> {
        val configs = HashMap<String, Any>(properties.buildConsumerProperties())
        configs[ConsumerConfig.CLIENT_ID_CONFIG] = consumerClientId
        return configs
    }
}
