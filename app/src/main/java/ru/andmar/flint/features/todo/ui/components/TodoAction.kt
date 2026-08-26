package ru.andmar.flint.features.todo.ui.components

import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.home.TodoScreenActions

interface TodoAction {

    data class FixTodo(val todoDetails: TodoDetails): TodoAction
    data class DoneTodo(val todoDetails: TodoDetails): TodoAction
    data class HighlightTodo(val todoDetails: TodoDetails): TodoAction
    data class EditTodo(val todoId: String): TodoAction
    data class DeleteTodo(val todoDetails: TodoDetails): TodoAction
    data class RestoreTodo(val todoDetails: TodoDetails): TodoAction
}