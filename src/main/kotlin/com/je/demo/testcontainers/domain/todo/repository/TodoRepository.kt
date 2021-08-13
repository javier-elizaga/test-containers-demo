package com.je.demo.testcontainers.domain.todo.repository

import com.je.demo.testcontainers.domain.todo.model.Todo

interface TodoRepository {
    fun get(id: Long): Todo
    fun create(todo: Todo): Todo
}
