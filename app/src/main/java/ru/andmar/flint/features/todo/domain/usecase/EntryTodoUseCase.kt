package ru.andmar.flint.features.todo.domain.usecase

import ru.andmar.flint.features.todo.data.repository.TodoRepository
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import java.util.UUID

class EntryTodoUseCase(private val todoRepository: TodoRepository) {

    suspend fun createTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.createTodo(
            todoDetails.copy(
                id = UUID.randomUUID().toString(),
                createTime = System.currentTimeMillis()
            )
        )
    }
}