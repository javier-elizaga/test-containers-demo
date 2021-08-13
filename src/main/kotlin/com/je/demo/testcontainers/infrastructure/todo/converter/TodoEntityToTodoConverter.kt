package com.je.demo.testcontainers.infrastructure.todo.converter

import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class TodoEntityToTodoConverter : Converter<TodoEntity, Todo> {
    override fun convert(it: TodoEntity) = Todo(
        id = it.id,
        description = it.description,
        isCompleted = it.isCompleted,
        dueDate = it.dueDate
    )
}
