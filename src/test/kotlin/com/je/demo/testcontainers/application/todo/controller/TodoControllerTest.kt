package com.je.demo.testcontainers.com.je.demo.testcontainers.application.todo.controller

import com.je.demo.testcontainers.application.todo.controller.TodoController
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.usecase.CreateTodoUseCase
import com.je.demo.testcontainers.domain.todo.usecase.GetTodoUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
internal class TodoControllerTest {

    @Mock
    lateinit var createTodoUseCase: CreateTodoUseCase

    @Mock
    lateinit var getTodoUseCase: GetTodoUseCase

    @InjectMocks
    lateinit var ctrl: TodoController

    @Test
    fun getTodo() {
        val id = 1L
        val todoDto = mock<TodoDto>()

        whenever(getTodoUseCase.getTodo(eq(id))).thenReturn(todoDto)

        val actual = ctrl.getTodo(id)

        assertNotNull(actual)
        assertEquals(HttpStatus.OK, actual.statusCode)
        assertEquals(todoDto, actual.body!!)
        verify(getTodoUseCase, times(1)).getTodo(eq(id))
    }

    @Test
    fun getTodoNotFound() {
        val id = 1L
        whenever(getTodoUseCase.getTodo(eq(id))).thenReturn(null)

        val actual = ctrl.getTodo(id)

        assertNotNull(actual)
        assertEquals(HttpStatus.NOT_FOUND, actual.statusCode)
        assertNull(actual.body)
        verify(getTodoUseCase, times(1)).getTodo(eq(id))
    }

    @Test
    fun createTodo() {
        val req = mock<CreateTodoDto>()
        val todo = mock<TodoDto>()

        whenever(createTodoUseCase.createTodo(any())).thenReturn(todo)

        val actual = ctrl.createTodo(req)

        assertNotNull(actual)
        assertEquals(todo, actual)
        verify(createTodoUseCase, times(1)).createTodo(any())
    }
}
