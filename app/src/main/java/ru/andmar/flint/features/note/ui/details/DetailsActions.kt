package ru.andmar.flint.features.note.ui.details

import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.todo.domain.model.TodoDetails


sealed interface DetailsActions {
    data class NoteActions(val noteAction: NoteAction): DetailsActions
    data class FixTodo(val todoDetails: TodoDetails): DetailsActions
    data class DoneTodo(val todoDetails: TodoDetails): DetailsActions
    data class HighlightTodo(val todoDetails: TodoDetails): DetailsActions
    data class DeleteTodo(val todoDetails: TodoDetails): DetailsActions
    object DismissError: DetailsActions
}