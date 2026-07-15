package com.andmar.flint.ui.theme.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditTodoViewModel(
    private val flintRepository: FlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val todoId: String = savedStateHandle.toRoute<EditTodoScreenRoute>().todoId

    var editTodoUiState by mutableStateOf(EditTodoUiState())
        private set

    init {
        viewModelScope.launch {
            editTodoUiState = EditTodoUiState(
                todoDetails = flintRepository.getTodoById(todoId)
                    .first()
            )
        }
    }

    fun onActions(editTodoActions: EditTodoActions) {
        when(editTodoActions) {
            is EditTodoActions.UpdateTodoDetails -> {
                updateTodoDetails(editTodoActions.todoDetails)
            }
            is EditTodoActions.EditTodo -> { editTodo() }
            is EditTodoActions.DismissError -> updateFlintActions(FlintActions.Default)
        }
    }

    private fun updateTodoDetails(todoDetails: TodoDetails) {
        editTodoUiState = EditTodoUiState(
            todoDetails = todoDetails,
            isAction = isTodoAction(todoDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        editTodoUiState = editTodoUiState.copy(
            flintActions = flintActions
        )
    }

    private fun editTodo() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.editTodo(
                    editTodoUiState.todoDetails
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }
}

data class EditTodoUiState(
    val todoDetails: TodoDetails = TodoDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EditTodoActions {
    data class UpdateTodoDetails(val todoDetails: TodoDetails): EditTodoActions
    object EditTodo: EditTodoActions
    object DismissError: EditTodoActions
}