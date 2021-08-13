package com.je.demo.testcontainers.domain.todo.error

class TodoNotFoundError : RuntimeException()
class TodoPersistenceError : RuntimeException()
class TodoUnexpectedError : RuntimeException()
class TodoBadRequestError : RuntimeException()
