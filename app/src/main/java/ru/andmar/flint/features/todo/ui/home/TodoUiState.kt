package ru.andmar.flint.features.todo.ui.home

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.todo.domain.model.TodoDetails

data class TodoUiState(
    val selectedTodoDetails: TodoDetails = TodoDetails(),
    val flintActions: FlintActions = FlintActions.Default
)