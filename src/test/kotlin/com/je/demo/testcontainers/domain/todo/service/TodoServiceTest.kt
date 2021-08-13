package com.je.demo.testcontainers.com.je.demo.testcontainers.domain.todo.service

import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.error.TodoBadRequestError
import com.je.demo.testcontainers.domain.todo.error.TodoUnexpectedError
import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.domain.todo.repository.TodoRepository
import com.je.demo.testcontainers.domain.todo.service.TodoService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.core.convert.converter.Converter

@ExtendWith(MockitoExtension::class)
internal class TodoServiceTest {
    @Mock
    lateinit var repo: TodoRepository

    @Mock
    lateinit var createTodoDtoToTodoConverter: Converter<CreateTodoDto, Todo?>

    @Mock
    lateinit var todoToTodoDtoConverter: Converter<Todo, TodoDto?>

    lateinit var srv: TodoService

    @BeforeEach
    internal fun setUp() {
        srv = TodoService(repo, createTodoDtoToTodoConverter, todoToTodoDtoConverter)
    }

    @Test
    fun getTodo() {
        val id = 1L
        val todo = mock<Todo>()
        val todoDto = mock<TodoDto>()
        whenever(repo.get(eq(id))).thenReturn(todo)
        whenever(todoToTodoDtoConverter.convert(eq(todo))).thenReturn(todoDto)

        val actual = srv.getTodo(id)

        assertEquals(todoDto, actual)

        verify(repo, times(1)).get(eq(id))
    }

    @Test
    fun getTodoTodoUnexpectedError() {
        val id = 1L
        val todo = mock<Todo>()
        whenever(repo.get(eq(id))).thenReturn(todo)
        whenever(todoToTodoDtoConverter.convert(eq(todo))).thenReturn(null)

        assertThrows(TodoUnexpectedError::class.java) { srv.getTodo(id) }
        verify(repo, times(1)).get(eq(id))
    }

    @Test
    fun createTodo() {
        val createTodoDto = mock<CreateTodoDto>()
        val todo = mock<Todo>()
        val createdTodo = mock<Todo>()
        val todoDto = mock<TodoDto>()
        whenever(createTodoDtoToTodoConverter.convert(eq(createTodoDto))).thenReturn(todo)
        whenever(repo.create(eq(todo))).thenReturn(createdTodo)
        whenever(todoToTodoDtoConverter.convert(eq(createdTodo))).thenReturn(todoDto)

        val actual = srv.createTodo(createTodoDto)

        assertEquals(todoDto, actual)
    }

    @Test
    fun createTodoBadRequestError() {
        val createTodoDto = mock<CreateTodoDto>()
        whenever(createTodoDtoToTodoConverter.convert(eq(createTodoDto))).thenReturn(null)

        assertThrows(TodoBadRequestError::class.java) { srv.createTodo(createTodoDto) }

        verify(repo, never()).create(any())
        verify(todoToTodoDtoConverter, never()).convert(any())
    }

    @Test
    fun createTodoTodoUnexpectedError() {
        val createTodoDto = mock<CreateTodoDto>()
        val todo = mock<Todo>()
        val createdTodo = mock<Todo>()
        whenever(createTodoDtoToTodoConverter.convert(eq(createTodoDto))).thenReturn(todo)
        whenever(repo.create(eq(todo))).thenReturn(createdTodo)
        whenever(todoToTodoDtoConverter.convert(eq(createdTodo))).thenReturn(null)

        assertThrows(TodoUnexpectedError::class.java) { srv.createTodo(createTodoDto) }
    }
}
