package com.je.demo.testcontainers.application.todo.controller

import com.je.demo.testcontainers.domain.todo.error.TodoNotFoundError
import com.je.demo.testcontainers.domain.todo.error.TodoPersistenceError
import com.je.demo.testcontainers.domain.todo.error.TodoUnexpectedError
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class TodoErrorController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TodoNotFoundError::class)
    fun handleNotFound() {
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(TodoPersistenceError::class)
    fun handlePersistenceError() {
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(TodoUnexpectedError::class)
    fun handleUnexpectedError() {
    }
}
