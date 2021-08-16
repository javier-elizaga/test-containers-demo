package com.je.demo.testcontainers.application.todo.kafka

import com.je.demo.testcontainers.avro.CreateTodoEvent
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class CreateTodoEventToCreateTodoDtoConverter : Converter<CreateTodoEvent, CreateTodoDto> {
    override fun convert(it: CreateTodoEvent) = CreateTodoDto(
        description = it.description
    )
}
