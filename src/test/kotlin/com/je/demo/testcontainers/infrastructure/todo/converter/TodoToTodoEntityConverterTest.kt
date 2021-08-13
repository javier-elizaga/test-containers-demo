package com.je.demo.testcontainers.com.je.demo.testcontainers.infrastructure.todo.converter

import com.je.demo.testcontainers.infrastructure.todo.converter.TodoToTodoEntityConverter
import com.je.demo.testcontainers.utils.TodoFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TodoToTodoEntityConverterTest {

    private val converter = TodoToTodoEntityConverter()

    @Test
    fun convert() {
        val todo = TodoFactory.create()

        val actual = converter.convert(todo)

        assertEquals(TodoFactory.id, actual.id)
        assertEquals(TodoFactory.description, actual.description)
        assertEquals(TodoFactory.isCompleted, actual.isCompleted)
        assertEquals(TodoFactory.dueDate, actual.dueDate)
    }
}
