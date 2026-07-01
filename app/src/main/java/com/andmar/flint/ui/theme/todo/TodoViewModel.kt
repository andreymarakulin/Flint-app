package com.andmar.flint.ui.theme.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TodoViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    val todoDetailsState: StateFlow<TodoDetailsState> =
        flintRepository.getTodos().map { todoItems ->
            TodoDetailsState(
                todoItems.sortedByDescending { it.fix }.map { todoItem ->
                    todoItem.toTodoDetails()
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TodoDetailsState()
        )

    var todoUiState by mutableStateOf(TodoUiState())
        private set

    fun onActions(todoActions: TodoActions) {
        when(todoActions) {
            is TodoActions.UpdateSelectedTodoDetails -> {
                updateSelectedTodoDetails(todoActions.todoDetails)
            }
            is TodoActions.FixTodo -> fixTodo()
            is TodoActions.DoneTodo -> doneTodo()
            is TodoActions.HighlightTodo -> highlightTodo()
            is TodoActions.DeleteTodo -> deleteTodo()
        }
    }

    private fun updateSelectedTodoDetails(todoDetails: TodoDetails) {
        todoUiState = TodoUiState(todoDetails)
    }

    private fun updateFlintActions(flintActions: FlintActions) {

    }

    private fun fixTodo() {
        val todoDetails = todoUiState.selectedTodoDetails

        flintRepository.editTodo(
            todoItem = todoDetails.copy(
                fix = !todoDetails.fix
            ).toTodoItem()
        ) { updateFlintActions(it) }
    }

    private fun doneTodo() {
        val todoDetails = todoUiState.selectedTodoDetails

        flintRepository.editTodo(
            todoItem = todoDetails.copy(
                done = !todoDetails.done
            ).toTodoItem()
        ) { updateFlintActions(it) }
    }

    private fun highlightTodo() {
        val todoDetails = todoUiState.selectedTodoDetails

        flintRepository.editTodo(
            todoItem = todoDetails.copy(
                highlight = !todoDetails.highlight
            ).toTodoItem()
        ) { updateFlintActions(it) }
    }

    private fun deleteTodo() {
        flintRepository.deleteTodo(
            todoItem = todoUiState.selectedTodoDetails.toTodoItem()
        ) { updateFlintActions(it) }
    }
}

data class TodoDetailsState(val todoDetailsList: List<TodoDetails> = emptyList())

data class TodoUiState(val selectedTodoDetails: TodoDetails = TodoDetails())

sealed interface TodoActions {
    data class UpdateSelectedTodoDetails(val todoDetails: TodoDetails): TodoActions
    object FixTodo: TodoActions
    object DoneTodo: TodoActions
    object HighlightTodo: TodoActions
    object DeleteTodo: TodoActions
}