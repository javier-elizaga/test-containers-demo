package com.je.demo.testcontainers.com.je.demo.testcontainers.infrastructure.todo

import com.je.demo.testcontainers.domain.todo.error.TodoNotFoundError
import com.je.demo.testcontainers.domain.todo.error.TodoPersistenceError
import com.je.demo.testcontainers.domain.todo.error.TodoUnexpectedError
import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.infrastructure.todo.JpaTodoRepository
import com.je.demo.testcontainers.infrastructure.todo.converter.TodoEntityToTodoConverter
import com.je.demo.testcontainers.infrastructure.todo.converter.TodoToTodoEntityConverter
import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import com.je.demo.testcontainers.infrastructure.todo.repository.SpringJpaTodoRepository
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.dao.DataIntegrityViolationException
import java.util.Optional

@ExtendWith(MockitoExtension::class)
internal class JpaTodoRepositoryTest {

    @Mock
    lateinit var repo: SpringJpaTodoRepository

    @Mock
    lateinit var todoToEntity: TodoToTodoEntityConverter

    @Mock
    lateinit var entityToTodo: TodoEntityToTodoConverter
    lateinit var jpaTodoRepository: JpaTodoRepository

    @BeforeEach
    internal fun setUp() {
        jpaTodoRepository = JpaTodoRepository(repo, todoToEntity, entityToTodo)
    }

    @Test
    fun get() {
        val id = 1L
        val todoEntity = mock<TodoEntity>()
        val todoEntityOpt = Optional.of(todoEntity)
        val todo = mock<Todo>()
        whenever(repo.findById(any())).thenReturn(todoEntityOpt)
        whenever(entityToTodo.convert(any())).thenReturn(todo)

        val actual = jpaTodoRepository.get(id)

        assertEquals(todo, actual)
    }

    @Test
    fun getNotFound() {
        val id = 1L
        val todoEntityOpt = Optional.empty<TodoEntity>()
        whenever(repo.findById(any())).thenReturn(todoEntityOpt)

        assertThrows(TodoNotFoundError::class.java) {
            jpaTodoRepository.get(id)
        }

        verify(entityToTodo, never()).convert(any())
    }

    @Test
    fun create() {
        val todo = mock<Todo>()
        val todoEntity = mock<TodoEntity>()

        whenever(todoToEntity.convert(eq(todo))).thenReturn(todoEntity)
        whenever(repo.save(eq(todoEntity))).thenReturn(todoEntity)
        whenever(entityToTodo.convert(eq(todoEntity))).thenReturn(todo)

        val actual = jpaTodoRepository.create(todo)

        assertEquals(todo, actual)
    }

    @Test
    fun createTodoToEntityError() {
        val todo = mock<Todo>()
        val todoEntity = mock<TodoEntity>()

        whenever(todoToEntity.convert(eq(todo))).thenReturn(null)

        assertThrows(TodoUnexpectedError::class.java) { jpaTodoRepository.create(todo) }

        verify(repo, never()).save(eq(todoEntity))
        verify(entityToTodo, never()).convert(eq(todoEntity))
    }

    @Test
    fun createTodoEntityToTodoError() {
        val todo = mock<Todo>()
        val todoEntity = mock<TodoEntity>()

        whenever(todoToEntity.convert(eq(todo))).thenReturn(todoEntity)
        whenever(repo.save(eq(todoEntity))).thenReturn(todoEntity)
        whenever(entityToTodo.convert(eq(todoEntity))).thenReturn(null)

        assertThrows(TodoUnexpectedError::class.java) { jpaTodoRepository.create(todo) }
    }

    @Test
    fun createTodoRepoError() {
        val todo = mock<Todo>()
        val todoEntity = mock<TodoEntity>()

        whenever(todoToEntity.convert(eq(todo))).thenReturn(todoEntity)
        whenever(repo.save(eq(todoEntity))).thenThrow(DataIntegrityViolationException(""))

        assertThrows(TodoPersistenceError::class.java) { jpaTodoRepository.create(todo) }

        verify(entityToTodo, never()).convert(eq(todoEntity))
    }
}
