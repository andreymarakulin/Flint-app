package com.andmar.flint.ui.theme.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(
    private val flintRepository: FlintRepository
): ViewModel() {

    val todoDetailsState: StateFlow<TodoDetailsState> =
        flintRepository.getTodos().map { todoItems ->
            TodoDetailsState(todoItems.sortedByDescending { it.fix })
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

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    todoDetails.copy(
                        fix = !todoDetails.fix
                    )
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun doneTodo() {
        val todoDetails = todoUiState.selectedTodoDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    todoDetails.copy(
                        done = !todoDetails.done
                    )
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun highlightTodo() {
        val todoDetails = todoUiState.selectedTodoDetails

        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    todoDetails.copy(
                        highlight = !todoDetails.highlight
                    )
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }

    private fun deleteTodo() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.deleteTodo(todoUiState.selectedTodoDetails)
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
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