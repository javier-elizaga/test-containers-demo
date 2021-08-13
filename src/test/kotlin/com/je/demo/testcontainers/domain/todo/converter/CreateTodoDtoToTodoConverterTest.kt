package com.je.demo.testcontainers.com.je.demo.testcontainers.domain.todo.converter

import com.je.demo.testcontainers.domain.todo.converter.CreateTodoDtoToTodoConverter
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class CreateTodoDtoToTodoConverterTest {

    private val converter = CreateTodoDtoToTodoConverter()

    @Test
    fun convert() {
        val description = "description"
        val isCompleted = true
        val dueDate = LocalDate.now()

        val dto = CreateTodoDto(
            description = description,
            isCompleted = isCompleted,
            dueDate = dueDate
        )

        val actual = converter.convert(dto) ?: return fail()

        assertNull(actual.id)
        assertEquals(description, actual.description)
        assertEquals(isCompleted, actual.isCompleted)
        assertEquals(dueDate, actual.dueDate)
    }

    @Test
    fun convertDefaultValues() {
        val description = "description"

        val dto = CreateTodoDto(description = description)

        val actual = converter.convert(dto) ?: return fail()

        assertNull(actual.id)
        assertEquals(description, actual.description)
        assertFalse(actual.isCompleted)
        assertNull(actual.dueDate)
    }

    @Test
    fun convertInvalidDto() {
        val dto = CreateTodoDto()

        val actual = converter.convert(dto)

        assertNull(actual)
    }
}
