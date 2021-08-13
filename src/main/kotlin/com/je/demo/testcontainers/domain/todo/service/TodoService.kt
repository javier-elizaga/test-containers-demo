package com.je.demo.testcontainers.domain.todo.service

import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.error.TodoBadRequestError
import com.je.demo.testcontainers.domain.todo.error.TodoUnexpectedError
import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.domain.todo.repository.TodoRepository
import com.je.demo.testcontainers.domain.todo.usecase.CreateTodoUseCase
import com.je.demo.testcontainers.domain.todo.usecase.GetTodoUseCase
import org.springframework.core.convert.converter.Converter
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class TodoService(
    private val repo: TodoRepository,
    private val createTodoDtoToTodoConverter: Converter<CreateTodoDto, Todo?>,
    private val todoToTodoDtoConverter: Converter<Todo, TodoDto?>,
) : CreateTodoUseCase, GetTodoUseCase {
    override fun createTodo(createTodoDto: CreateTodoDto): TodoDto {
        val todo = convertOrThrow(createTodoDto, TodoBadRequestError::class)
        val savedTodo = repo.create(todo)
        return convertOrThrow(savedTodo, TodoUnexpectedError::class)
    }

    override fun getTodo(id: Long): TodoDto {
        val todo = repo.get(id)
        return convertOrThrow(todo, TodoUnexpectedError::class)
    }

    private fun <T : RuntimeException> convertOrThrow(
        createTodoDto: CreateTodoDto,
        kClass: KClass<T>
    ): Todo {
        return createTodoDtoToTodoConverter.convert(createTodoDto) ?: throw kClass.createInstance()
    }

    private fun <T : RuntimeException> convertOrThrow(todo: Todo, kClass: KClass<T>): TodoDto {
        return todoToTodoDtoConverter.convert(todo) ?: throw kClass.createInstance()
    }
}
