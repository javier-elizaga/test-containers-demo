package com.je.demo.testcontainers.application.todo.controller

import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.usecase.CreateTodoUseCase
import com.je.demo.testcontainers.domain.todo.usecase.GetTodoUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

const val GetRequestMappingUrl = "/todos/{id}"
const val PostRequestMappingUrl = "/todos"

@RestController
class TodoController(
    private val createTodoUseCase: CreateTodoUseCase,
    private val getTodoUseCase: GetTodoUseCase
) {

    @GetMapping(GetRequestMappingUrl)
    fun getTodo(@PathVariable(value = "id") id: Long): ResponseEntity<TodoDto> {
        val todo = getTodoUseCase.getTodo(id)
        return ResponseEntity.of(Optional.ofNullable(todo))
    }

    @PostMapping(PostRequestMappingUrl)
    fun createTodo(@RequestBody createTodoDto: CreateTodoDto): TodoDto {
        return createTodoUseCase.createTodo(createTodoDto)
    }
}
