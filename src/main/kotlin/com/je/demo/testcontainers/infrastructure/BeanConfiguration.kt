package com.je.demo.testcontainers.infrastructure

import com.je.demo.testcontainers.domain.todo.converter.CreateTodoDtoToTodoConverter
import com.je.demo.testcontainers.domain.todo.converter.TodoToTodoDtoConverter
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.domain.todo.repository.TodoRepository
import com.je.demo.testcontainers.domain.todo.service.TodoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

@Configuration
class BeanConfiguration {

    @Bean
    fun createTodoDtoToTodoConverter() = CreateTodoDtoToTodoConverter()

    @Bean
    fun todoToTodoDtoConverter() = TodoToTodoDtoConverter()

    @Bean
    fun todoService(
        repo: TodoRepository,
        createTodoDtoToTodoConverter: Converter<CreateTodoDto, Todo?>,
        todoToTodoDtoConverter: Converter<Todo, TodoDto?>,
    ) = TodoService(repo, createTodoDtoToTodoConverter, todoToTodoDtoConverter)
}
