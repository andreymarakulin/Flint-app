package ru.andmar.flint.features.todo.ui.home

import ru.andmar.flint.features.todo.domain.model.TodoDetails

data class TodoDetailsState(
    val todoDetailsList: List<TodoDetails> = emptyList(),
    val todoDetailsDoneList: List<TodoDetails> = emptyList()
)