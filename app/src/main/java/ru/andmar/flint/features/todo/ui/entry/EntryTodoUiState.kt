package ru.andmar.flint.features.todo.ui.entry

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.todo.domain.model.TodoDetails

data class EntryTodoUiState(
    val todoDetails: TodoDetails = TodoDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)