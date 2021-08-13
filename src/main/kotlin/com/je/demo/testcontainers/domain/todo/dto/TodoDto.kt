package com.je.demo.testcontainers.domain.todo.dto

import java.time.LocalDate

data class TodoDto(
    val id: Long,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: LocalDate? = null
)
