package ru.andmar.flint.features.todo.ui.entry

import ru.andmar.flint.features.todo.domain.model.TodoDetails

sealed interface EntryTodoScreenActions {
    data class UpdateTodoScreenDetails(val todoDetails: TodoDetails): EntryTodoScreenActions
    object CreateTodoScreen: EntryTodoScreenActions
    object DismissError: EntryTodoScreenActions
}