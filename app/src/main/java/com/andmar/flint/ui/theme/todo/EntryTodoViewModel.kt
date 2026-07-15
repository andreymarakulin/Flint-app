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
import kotlinx.coroutines.launch

class EntryTodoViewModel(
    private val flintRepository: FlintRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle.toRoute<EntryTodoScreenRoute>().noteId

    var entryTodoUiState by mutableStateOf(EntryTodoUiState())
        private set

    fun onActions(entryTodoActions: EntryTodoActions) {
        when(entryTodoActions) {
            is EntryTodoActions.UpdateTodoDetails -> {
                updateTodoDetails(entryTodoActions.todoDetails)
            }
            is EntryTodoActions.CreateTodo -> {createTodo() }
            is EntryTodoActions.DismissError -> updateFlintActions(FlintActions.Default)
        }
    }

    private fun updateTodoDetails(todoDetails: TodoDetails) {
        entryTodoUiState = EntryTodoUiState(
            todoDetails = todoDetails,
            isAction = isTodoAction(todoDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        entryTodoUiState = entryTodoUiState.copy(
            flintActions = flintActions
        )
    }

    private fun createTodo() {
        viewModelScope.launch {
            updateFlintActions(FlintActions.Loading)
            try {
                flintRepository.createTodo(
                    entryTodoUiState.todoDetails
                        .copy(noteId = noteId)
                )
                updateFlintActions(FlintActions.Success)
            } catch (e: Exception) {
                updateFlintActions(FlintActions.Error(e.message ?: "Error"))
            }
        }
    }
}

data class EntryTodoUiState(
    val todoDetails: TodoDetails = TodoDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EntryTodoActions {
    data class UpdateTodoDetails(val todoDetails: TodoDetails): EntryTodoActions
    object CreateTodo: EntryTodoActions
    object DismissError: EntryTodoActions
}

data class TodoDetails(
    val id: String = "",
    val noteId: String = "",
    val title: String = "",
    val text: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)

fun isTodoAction(todoDetails: TodoDetails): Boolean {
    return todoDetails.title.isNotBlank() || todoDetails.text.isNotBlank()
}