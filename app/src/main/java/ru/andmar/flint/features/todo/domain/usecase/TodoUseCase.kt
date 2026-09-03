package ru.andmar.flint.features.todo.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.features.todo.data.repository.TodoRepository
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class TodoUseCase(private val todoRepository: TodoRepository) {

    fun getTodos(): Flow<List<TodoDetails>> = todoRepository.getTodos()
}