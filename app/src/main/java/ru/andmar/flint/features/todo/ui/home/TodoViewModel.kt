package ru.andmar.flint.features.todo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.note.ui.home.NoteUiAction
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.domain.usecase.TodoActionsUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoUseCase
import ru.andmar.flint.features.todo.ui.components.TodoAction

class TodoViewModel(
    private val todoUseCase: TodoUseCase,
    private val todoActionsUseCase: TodoActionsUseCase
): ViewModel() {

    val todoDetailsState: StateFlow<TodoDetailsState> =
        todoUseCase.getTodos().map { todoItems ->
            TodoDetailsState(
                todoItems.filter{ !it.deleted }.sortedWith(
                compareByDescending<TodoDetails> { it.fix }
                    .thenByDescending { it.updateTime }
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TodoDetailsState()
        )

    private val _todoUiState = MutableStateFlow(TodoUiState())
    val todoUiState: StateFlow<TodoUiState> = _todoUiState

    private val _todoUiAction = Channel<TodoUiAction>()
    val todoUiAction = _todoUiAction.receiveAsFlow()

    fun onActions(todoScreenActions: TodoScreenActions) {
        when(todoScreenActions) {
            is TodoScreenActions.UpdateSelectedTodoDetails -> {
                _todoUiState.update {
                    it.copy(selectedTodoDetails = todoScreenActions.todoDetails)
                }
            }
            is TodoScreenActions.TodoActions -> {
                when(val todoAction = todoScreenActions.todoAction) {
                    is TodoAction.FixTodo -> {
                        flintActions {
                            todoActionsUseCase.fixTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.DoneTodo -> {
                        flintActions {
                            todoActionsUseCase.doneTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.HighlightTodo -> {
                        flintActions {
                            todoActionsUseCase.highlightTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.EditTodo -> {
                        viewModelScope.launch {
                            _todoUiAction.send(
                                TodoUiAction.EditTodo(
                                    todoAction.todoId
                                )
                            )
                        }
                    }
                    is TodoAction.DeleteTodo -> {
                        flintActions {
                            todoActionsUseCase.deleteTodo(todoAction.todoDetails)
                        }
                        viewModelScope.launch {
                            _todoUiAction.send(
                                TodoUiAction
                                    .ShowDeleteSnackbar(todoAction.todoDetails)
                            )
                        }
                    }
                }
            }
            is TodoScreenActions.DismissError -> {
                _todoUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _todoUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _todoUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _todoUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}