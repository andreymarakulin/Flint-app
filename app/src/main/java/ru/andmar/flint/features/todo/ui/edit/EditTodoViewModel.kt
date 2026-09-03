package ru.andmar.flint.features.todo.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.todo.domain.model.isTodoAction
import ru.andmar.flint.features.todo.domain.usecase.EditTodoUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EditTodoViewModel(
    private val editTodoUseCase: EditTodoUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val todoId: String = savedStateHandle.toRoute<NavigationRoutes.EditTodoScreenRoute>().todoId

    private val _editTodoUiState = MutableStateFlow(EditTodoUiState())
    val editTodoUiState: StateFlow<EditTodoUiState> = _editTodoUiState

    init {
        viewModelScope.launch {
            _editTodoUiState.update {
                it.copy(todoDetails = editTodoUseCase.getTodoById(todoId))
            }
        }
    }

    fun onActions(editTodoScreenActions: EditTodoScreenActions) {
        when(editTodoScreenActions) {
            is EditTodoScreenActions.UpdateTodoScreenDetails -> {
                _editTodoUiState.update {
                    it.copy(
                        todoDetails = editTodoScreenActions.todoDetails,
                        isAction = isTodoAction(editTodoScreenActions.todoDetails)
                    )
                }
            }
            is EditTodoScreenActions.EditTodoScreen -> {
                flintActions {
                    editTodoUseCase.editTodo(_editTodoUiState.value.todoDetails)
                }
            }
            is EditTodoScreenActions.DismissError -> {
                _editTodoUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _editTodoUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _editTodoUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _editTodoUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}