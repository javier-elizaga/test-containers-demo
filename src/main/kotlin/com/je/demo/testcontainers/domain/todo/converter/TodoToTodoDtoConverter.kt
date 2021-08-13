package com.je.demo.testcontainers.domain.todo.converter

import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.model.Todo
import org.springframework.core.convert.converter.Converter

class TodoToTodoDtoConverter : Converter<Todo, TodoDto?> {
    override fun convert(it: Todo): TodoDto? {
        if (it.id == null) return null
        return TodoDto(
            id = it.id,
            description = it.description,
            isCompleted = it.isCompleted,
            dueDate = it.dueDate,
        )
    }
}
