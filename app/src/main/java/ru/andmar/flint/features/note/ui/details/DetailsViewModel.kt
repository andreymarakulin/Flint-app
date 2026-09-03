package ru.andmar.flint.features.note.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.note.domain.usecase.DetailsUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteActionsUseCase
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.todo.domain.usecase.TodoActionsUseCase
import ru.andmar.flint.features.todo.ui.components.TodoAction
import ru.andmar.flint.features.todo.ui.home.TodoDetailsState
import ru.andmar.flint.navigation.NavigationRoutes

class DetailsViewModel(
    private val detailsUseCase: DetailsUseCase,
    private val noteActionsUseCase: NoteActionsUseCase,
    private val todoActionsUseCase: TodoActionsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteId: String = savedStateHandle.toRoute< NavigationRoutes.DetailsScreenRoute>().noteId

    val noteDetailsState: StateFlow<NoteDetailsState> =
        detailsUseCase.getNoteById(noteId).map { noteDetails ->
            NoteDetailsState(noteDetails)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NoteDetailsState()
        )

    val todoDetailsState: StateFlow<TodoDetailsState> =
        detailsUseCase.getTodos().map { todoItems ->
            TodoDetailsState(
                todoItems.filter { it.noteId == noteId }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TodoDetailsState()
        )

    private val _detailsUiState = MutableStateFlow(DetailsUiState())
    val detailsUiState: StateFlow<DetailsUiState> = _detailsUiState

    private val _detailsUiAction = Channel<DetailsUiAction>()
    val detailsUiAction = _detailsUiAction.receiveAsFlow()


    fun onActions(detailsScreenActions: DetailsScreenActions) {
        when(detailsScreenActions) {
            is DetailsScreenActions.NoteActions -> {
                when(val noteAction = detailsScreenActions.noteAction) {
                    is NoteAction.FixNote -> {
                        flintAction {
                            noteActionsUseCase.fixNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.DoneNote -> {
                        flintAction {
                            noteActionsUseCase.doneNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.HighlightNote -> {
                        flintAction {
                            noteActionsUseCase.highlightNote(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.EditNote -> {
                        viewModelScope.launch {
                            _detailsUiAction.send(
                                DetailsUiAction.EditNote(noteAction.noteId)
                            )
                        }
                    }
                    is NoteAction.DeleteNote -> {
                        flintAction {
                            noteActionsUseCase.updateDeleteNoteState(noteAction.noteDetails)
                        }
                    }
                    is NoteAction.RestoreNote -> {

                    }
                }
            }
            is DetailsScreenActions.TodoActions -> {
                when(val todoAction = detailsScreenActions.todoAction) {
                    is TodoAction.FixTodo -> {
                        flintAction {
                            todoActionsUseCase.fixTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.DoneTodo -> {
                        flintAction {
                            todoActionsUseCase.doneTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.HighlightTodo -> {
                        flintAction {
                            todoActionsUseCase.highlightTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.EditTodo -> {
                        viewModelScope.launch {
                            _detailsUiAction.send(
                                DetailsUiAction.EditTodo(todoAction.todoId)
                            )
                        }
                    }
                    is TodoAction.DeleteTodo -> {
                        flintAction {
                            todoActionsUseCase.updateTodoDeleteState(todoAction.todoDetails)
                        }
                    }
                }
            }
            is DetailsScreenActions.DismissError -> {
                _detailsUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _detailsUiState.update { uiState ->
                uiState.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _detailsUiState.update { it.copy(flintActions = FlintActions.Success) }
            }.onFailure { e ->
                _detailsUiState.update { it.copy(flintActions = FlintActions.Error(e.message ?:"Error")) }
            }
        }
    }
}