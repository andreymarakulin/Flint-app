package ru.andmar.flint.features.todo.ui.home

import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.home.NoteUiAction
import ru.andmar.flint.features.todo.domain.model.TodoDetails


sealed interface TodoUiAction {
    object None: TodoUiAction
    object ChoiceLabelSheet: TodoUiAction
    data class EditTodo(val todoId: String): TodoUiAction
    data class ShowDeleteSnackbar(val todoDetails: TodoDetails): TodoUiAction
}