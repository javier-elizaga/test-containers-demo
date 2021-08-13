package com.je.demo.testcontainers.domain.todo.converter

import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.model.Todo
import org.springframework.core.convert.converter.Converter

class CreateTodoDtoToTodoConverter : Converter<CreateTodoDto, Todo?> {
    override fun convert(it: CreateTodoDto): Todo? {
        if (it.description == null) return null
        return Todo(
            description = it.description,
            isCompleted = it.isCompleted ?: false,
            dueDate = it.dueDate,
        )
    }
}
