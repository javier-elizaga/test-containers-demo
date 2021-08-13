package com.je.demo.testcontainers.utils

import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import java.time.LocalDate

class TodoEntityFactory {
    companion object {
        val id = 1L
        val description = "description"
        val isCompleted = true
        val dueDate = LocalDate.now()!!

        fun create() = TodoEntity(
            id = id,
            description = description,
            isCompleted = isCompleted,
            dueDate = dueDate
        )
    }
}
