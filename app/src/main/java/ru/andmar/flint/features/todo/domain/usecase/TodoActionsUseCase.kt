package ru.andmar.flint.features.todo.domain.usecase

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.todo.data.repository.TodoRepository
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class TodoActionsUseCase(private val todoRepository: TodoRepository) {

    suspend fun fixTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(fix = !todoDetails.fix)
        )
    }

    suspend fun doneTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(done = !todoDetails.done)
        )
    }

    suspend fun highlightTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(highlight = !todoDetails.highlight)
        )
    }
    suspend fun archiveTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(archive = true)
        )
    }

    suspend fun editLabel(todoDetails: TodoDetails, labelDetails: LabelDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(labelDetails = labelDetails)
        )
    }

    suspend fun updateTodoDeleteState(todoDetails: TodoDetails) = runCatching {
        todoRepository.editTodo(
            todoDetails.copy(deleted = !todoDetails.deleted)
        )
    }

    suspend fun deleteTodo(todoDetails: TodoDetails) = runCatching {
        todoRepository.deleteTodo(todoDetails)
    }
}