package com.je.demo.testcontainers.application.todo.kafka.config

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ErrorHandler

abstract class KafkaContainerFactoryBase {

    fun containerFactory(
        configurer: ConcurrentKafkaListenerContainerFactoryConfigurer,
        kafkaProperties: KafkaProperties,
        errorHandler: ErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<Any, Any> {
        val consumerFactory = consumerFactory(kafkaProperties)
        val factory = ConcurrentKafkaListenerContainerFactory<Any, Any>()
        configurer.configure(factory, consumerFactory)
        factory.consumerFactory = consumerFactory
        factory.setErrorHandler(errorHandler)
        return factory
    }

    private fun consumerFactory(kafkaProperties: KafkaProperties): ConsumerFactory<Any, Any> {
        return DefaultKafkaConsumerFactory(consumerConfig(kafkaProperties))
    }

    abstract fun consumerConfig(properties: KafkaProperties): Map<String, Any>
}
