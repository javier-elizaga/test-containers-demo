package com.je.demo.testcontainers.infrastructure.todo

import com.je.demo.testcontainers.domain.todo.error.TodoNotFoundError
import com.je.demo.testcontainers.domain.todo.error.TodoPersistenceError
import com.je.demo.testcontainers.domain.todo.error.TodoUnexpectedError
import com.je.demo.testcontainers.domain.todo.model.Todo
import com.je.demo.testcontainers.domain.todo.repository.TodoRepository
import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import com.je.demo.testcontainers.infrastructure.todo.repository.SpringJpaTodoRepository
import org.springframework.core.convert.converter.Converter
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class JpaTodoRepository(
    private val repo: SpringJpaTodoRepository,
    private val todoToEntity: Converter<Todo, TodoEntity>,
    private val entityToTodo: Converter<TodoEntity, Todo>
) : TodoRepository {

    override fun get(id: Long) = repo.findById(id)
        .map(entityToTodo::convert)
        .orElse(null) ?: throw TodoNotFoundError()

    override fun create(todo: Todo): Todo {
        try {
            val entity = todoToEntity.convert(todo) ?: throw TodoUnexpectedError()
            val savedEntity = repo.save(entity)
            return entityToTodo.convert(savedEntity) ?: throw TodoUnexpectedError()
        } catch (e: DataAccessException) {
            throw TodoPersistenceError()
        }
    }
}
