package com.je.demo.testcontainers.domain.todo.usecase

import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto

interface CreateTodoUseCase {
    fun createTodo(createTodoDto: CreateTodoDto): TodoDto
}
