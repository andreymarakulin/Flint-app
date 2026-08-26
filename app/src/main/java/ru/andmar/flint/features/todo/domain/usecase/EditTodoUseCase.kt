package ru.andmar.flint.features.todo.domain.usecase

import ru.andmar.flint.features.todo.data.repository.TodoRepository
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class EditTodoUseCase(private val todoRepository: TodoRepository) {

    suspend fun getTodoById(todoId: String): TodoDetails {
        return todoRepository.getTodoByIdOnce(todoId)
    }

    suspend fun editTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(todoDetails)
    }
}