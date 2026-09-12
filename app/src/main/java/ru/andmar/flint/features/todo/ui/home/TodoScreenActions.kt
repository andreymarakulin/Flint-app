package ru.andmar.flint.features.todo.ui.home

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.ui.home.NoteScreenActions
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.components.TodoAction


sealed interface TodoScreenActions {
    data class UpdateSelectedTodoDetails(val todoDetails: TodoDetails): TodoScreenActions
    data class TodoActions(val todoAction: TodoAction): TodoScreenActions
    data class EditLabel(val labelDetails: LabelDetails): TodoScreenActions
    object DismissError: TodoScreenActions
}