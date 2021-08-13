package com.je.demo.testcontainers.domain.todo.dto

import java.time.LocalDate

data class CreateTodoDto(
    val description: String? = null,
    val isCompleted: Boolean? = null,
    val dueDate: LocalDate? = null
)
