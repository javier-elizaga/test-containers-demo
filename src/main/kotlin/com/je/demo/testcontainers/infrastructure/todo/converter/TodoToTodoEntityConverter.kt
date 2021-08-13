package com.je.demo.testcontainers.infrastructure.todo.converter

import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class TodoToTodoEntityConverter : Converter<Todo, TodoEntity> {
    override fun convert(it: Todo) = TodoEntity(
        id = it.id,
        description = it.description,
        isCompleted = it.isCompleted,
        dueDate = it.dueDate
    )
}
