package com.je.demo.testcontainers.com.je.demo.testcontainers.domain.todo.converter

import com.je.demo.testcontainers.domain.todo.converter.TodoToTodoDtoConverter
import com.je.demo.testcontainers.utils.TodoFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class TodoToTodoDtoConverterTest {

    private val converter = TodoToTodoDtoConverter()

    @Test
    fun convert() {
        val todo = TodoFactory.create()

        val actual = converter.convert(todo) ?: return fail()

        assertEquals(TodoFactory.id, actual.id)
        assertEquals(TodoFactory.description, actual.description)
        assertEquals(TodoFactory.isCompleted, actual.isCompleted)
        assertEquals(TodoFactory.dueDate, actual.dueDate)
    }

    @Test
    fun convertInvalidDto() {
        val todo = TodoFactory.createWithNoId()

        val actual = converter.convert(todo)

        assertNull(actual)
    }
}
