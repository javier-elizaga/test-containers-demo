package com.je.demo.testcontainers.com.je.demo.testcontainers.application.todo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.je.demo.testcontainers.application.todo.controller.GetRequestMappingUrl
import com.je.demo.testcontainers.application.todo.controller.PostRequestMappingUrl
import com.je.demo.testcontainers.application.todo.controller.TodoController
import com.je.demo.testcontainers.domain.todo.dto.CreateTodoDto
import com.je.demo.testcontainers.domain.todo.dto.TodoDto
import com.je.demo.testcontainers.domain.todo.usecase.CreateTodoUseCase
import com.je.demo.testcontainers.domain.todo.usecase.GetTodoUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TodoController::class])
internal class TodoControllerMockMvcTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var createTodoUseCase: CreateTodoUseCase

    @MockBean
    lateinit var getTodoUseCase: GetTodoUseCase

    @Test
    fun getTodo() {
        val id = 1L
        val todo = TodoDto(id, "description", true, LocalDate.now())
        whenever(getTodoUseCase.getTodo(eq(id))).thenReturn(todo)

        val request = get(GetRequestMappingUrl, id)
            .contentType(APPLICATION_JSON)
        val mvcResult = mockMvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()

        val actualResponseBody = mvcResult.response.contentAsString
        assertEquals(objectMapper.writeValueAsString(todo), actualResponseBody)
    }

    @Test
    fun getTodoNotFound() {
        val id = 1L
        whenever(getTodoUseCase.getTodo(eq(id))).thenReturn(null)

        val request = get(GetRequestMappingUrl, id)
            .contentType(APPLICATION_JSON)
        val mvcResult = mockMvc.perform(request)
            .andExpect(status().isNotFound)
            .andReturn()

        val actualResponseBody = mvcResult.response.contentAsString
        assertEquals("", actualResponseBody)
    }

    @Test
    fun createTodo() {
        val id = 1L
        val requestContent = CreateTodoDto("description")
        val todo = TodoDto(id, "description", false)

        whenever(createTodoUseCase.createTodo(eq(requestContent))).thenReturn(todo)

        val request = post(PostRequestMappingUrl)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestContent))
        val mvcResult = mockMvc.perform(request)
            .andExpect(status().isOk)
            .andReturn()

        val actualResponseBody = mvcResult.response.contentAsString
        assertEquals(objectMapper.writeValueAsString(todo), actualResponseBody)
    }
}
