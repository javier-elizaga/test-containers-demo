package com.je.demo.testcontainers.utils

import com.je.demo.testcontainers.domain.todo.model.Todo
import java.time.LocalDate

class TodoFactory {
    companion object {
        val id = 1L
        val description = "description"
        val isCompleted = true
        val dueDate = LocalDate.now()!!

        fun create() = Todo(
            id = id,
            description = description,
            isCompleted = isCompleted,
            dueDate = dueDate
        )

        fun createWithNoId() = Todo(
            description = description,
            isCompleted = isCompleted,
            dueDate = dueDate
        )
    }
}
