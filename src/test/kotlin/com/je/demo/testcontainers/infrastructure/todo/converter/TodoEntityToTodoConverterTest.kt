package com.je.demo.testcontainers.com.je.demo.testcontainers.infrastructure.todo.converter

import com.je.demo.testcontainers.infrastructure.todo.converter.TodoEntityToTodoConverter
import com.je.demo.testcontainers.utils.TodoEntityFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TodoEntityToTodoConverterTest {

    private val converter = TodoEntityToTodoConverter()

    @Test
    fun convert() {
        val todo = TodoEntityFactory.create()

        val actual = converter.convert(todo)

        assertEquals(TodoEntityFactory.id, actual.id)
        assertEquals(TodoEntityFactory.description, actual.description)
        assertEquals(TodoEntityFactory.isCompleted, actual.isCompleted)
        assertEquals(TodoEntityFactory.dueDate, actual.dueDate)
    }
}
