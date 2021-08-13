package com.je.demo.testcontainers.infrastructure.todo.model

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

private const val GeneratorName = "TodoGen"
private const val SequenceName = "TODO_SEQ"

@Entity(name = "todo")
class TodoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GeneratorName)
    @SequenceGenerator(name = GeneratorName, sequenceName = SequenceName, allocationSize = 1)
    val id: Long? = null,
    val description: String,
    val isCompleted: Boolean,
    val dueDate: LocalDate? = null
)
