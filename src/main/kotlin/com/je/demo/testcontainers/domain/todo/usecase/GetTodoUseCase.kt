package com.je.demo.testcontainers.domain.todo.usecase

import com.je.demo.testcontainers.domain.todo.dto.TodoDto

interface GetTodoUseCase {
    fun getTodo(id: Long): TodoDto?
}
