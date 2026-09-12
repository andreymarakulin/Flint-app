package ru.andmar.flint.features.note.ui.details

import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.components.TodoAction


sealed interface DetailsScreenActions {
    data class NoteActions(val noteAction: NoteAction): DetailsScreenActions
    data class DeleteNoteLabel(val noteDetails: NoteDetails): DetailsScreenActions
    data class TodoActions(val todoAction: TodoAction): DetailsScreenActions
    object DismissError: DetailsScreenActions
}