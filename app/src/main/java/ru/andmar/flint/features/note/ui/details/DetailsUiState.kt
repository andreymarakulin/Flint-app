package ru.andmar.flint.features.note.ui.details

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.todo.domain.model.TodoDetails

data class DetailsUiState(
    val selectedTodoDetails: TodoDetails = TodoDetails(),
    val flintActions: FlintActions = FlintActions.Default
)
