package ru.andmar.flint.features.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.domain.usecase.LabelUseCase
import ru.andmar.flint.features.label.ui.home.LabelDetailsListState
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.domain.usecase.TodoActionsUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoUseCase
import ru.andmar.flint.features.todo.ui.components.TodoAction
import kotlin.collections.map
import kotlin.collections.toSet

class TodoViewModel(
    private val todoUseCase: TodoUseCase,
    private val todoActionsUseCase: TodoActionsUseCase,
    private val labelUseCase: LabelUseCase
): ViewModel() {

    val choiceLabelDetailsList: StateFlow<List<LabelDetails>> =
        labelUseCase.getLabels().map { labelDetails ->
            labelDetails.filter { it.choice }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val todoDetailsList: StateFlow<List<TodoDetails>> =
        todoUseCase.getTodos().map { todoDetails ->
            todoDetails.filter { !it.archive }.filter { !it.deleted }.sortedWith(
                compareByDescending<TodoDetails> { it.fix }
                    .thenByDescending { it.updateTime }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val todoDetailsState: StateFlow<TodoDetailsState> = combine(
        choiceLabelDetailsList,
        todoDetailsList
    ) { labels, todos ->
        val filterTodoByLabels = if (labels.isNotEmpty()) {
            val selectedLabelIds = labels.map { it.id }.toSet()
            todos.filter { it.labelDetails.id in selectedLabelIds }
        } else todos

        val (done, active) = filterTodoByLabels.partition { it.done }

        TodoDetailsState(
            todoDetailsList = active,
            todoDetailsDoneList = done
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TodoDetailsState()
    )

    val labelDetailsListState: StateFlow<LabelDetailsListState> =
        labelUseCase.getLabels().map { labelDetails ->
            LabelDetailsListState(
                labelDetails.filter { !it.archive }.filter { !it.deleted }.sortedWith(
                    compareByDescending<LabelDetails> { it.fix }
                        .thenByDescending { it.updateTime }
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LabelDetailsListState()
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
                    is TodoAction.ArchiveTodo -> {
                        flintActions {
                            todoActionsUseCase.archiveTodo(todoAction.todoDetails)
                        }
                    }
                    is TodoAction.EditLabel -> {
                        viewModelScope.launch {
                            _todoUiAction.send(TodoUiAction.ChoiceLabelSheet)
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
            is TodoScreenActions.EditLabel -> {
                flintActions {
                    todoActionsUseCase.editLabel(
                        _todoUiState.value.selectedTodoDetails,
                        todoScreenActions.labelDetails
                    )
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