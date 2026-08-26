package ru.andmar.flint.features.todo.ui.edit

import ru.andmar.flint.features.todo.domain.model.TodoDetails

sealed interface EditTodoScreenActions {
    data class UpdateTodoScreenDetails(val todoDetails: TodoDetails): EditTodoScreenActions
    object EditTodoScreen: EditTodoScreenActions
    object DismissError: EditTodoScreenActions
}