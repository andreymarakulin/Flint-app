package ru.andmar.flint.features.todo.ui.entry

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
import ru.andmar.flint.features.todo.domain.usecase.EntryTodoUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EntryTodoViewModel(
    private val entryTodoUseCase: EntryTodoUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle
        .toRoute<NavigationRoutes.EntryTodoScreenRoute>()
        .noteId

    private val _entryTodoUiState = MutableStateFlow(EntryTodoUiState())
    val entryTodoUiState: StateFlow<EntryTodoUiState> = _entryTodoUiState
    fun onActions(entryTodoScreenActions: EntryTodoScreenActions) {
        when (entryTodoScreenActions) {
            is EntryTodoScreenActions.UpdateTodoScreenDetails -> {
                _entryTodoUiState.update {
                    it.copy(
                        todoDetails = entryTodoScreenActions.todoDetails,
                        isAction = isTodoAction(entryTodoScreenActions.todoDetails)
                    )
                }
            }

            is EntryTodoScreenActions.CreateTodoScreen -> {
                flintActions {
                    entryTodoUseCase.createTodo(
                        _entryTodoUiState.value.todoDetails.copy(
                            noteId = noteId
                        )
                    )
                }
            }

            is EntryTodoScreenActions.DismissError -> {
                _entryTodoUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _entryTodoUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _entryTodoUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _entryTodoUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}