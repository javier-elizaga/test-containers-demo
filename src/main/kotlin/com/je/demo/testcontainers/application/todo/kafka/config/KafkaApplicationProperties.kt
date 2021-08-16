package com.je.demo.testcontainers.application.todo.kafka.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

// list of container-factory bean names
const val CreateTodoContainerFactory = "create-todo-container-factory"

@Component
@ConfigurationProperties(prefix = "application.kafka")
data class KafkaApplicationProperties(
    // @NestedConfigurationProperty
    var createTodo: KafkaApplicationTopicProperties? = null
) {
    val topicNames: List<String> get() = listOf(createTodo!!.topic)
}

data class KafkaApplicationTopicProperties(
    var topic: String = "",
    var consumerClientId: String = "",
    var consumerGroupId: String = "",
)
