package com.je.demo.testcontainers.domain.todo.model

import java.time.LocalDate

class Todo(
    val id: Long? = null,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: LocalDate? = null
)
