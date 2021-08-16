package com.je.demo.testcontainers.application.todo.kafka

import com.je.demo.testcontainers.avro.CreateTodoEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CreateTodoEventToCreateTodoDtoConverterTest {

    private val converter = CreateTodoEventToCreateTodoDtoConverter()

    @Test
    fun convert() {
        val description = "description"
        val event = CreateTodoEvent(description)
        val actual = converter.convert(event)
        assertEquals(description, actual.description)
    }
}
