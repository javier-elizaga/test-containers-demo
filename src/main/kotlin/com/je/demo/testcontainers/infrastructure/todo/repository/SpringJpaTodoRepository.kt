package com.je.demo.testcontainers.infrastructure.todo.repository

import com.je.demo.testcontainers.infrastructure.todo.model.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SpringJpaTodoRepository : JpaRepository<TodoEntity, Long> {
    override fun findById(id: Long): Optional<TodoEntity>
}
