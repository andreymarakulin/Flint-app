package ru.andmar.flint.features.basket.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.archive.ui.ArchiveScreenActions
import ru.andmar.flint.features.archive.ui.ArchiveUiState
import ru.andmar.flint.features.basket.domain.usecase.BasketUseCase
import ru.andmar.flint.features.category.domain.usecase.CategoryUseCase
import ru.andmar.flint.features.label.domain.usecase.LabelUseCase
import ru.andmar.flint.features.note.domain.usecase.NoteUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderUseCase
import ru.andmar.flint.features.todo.domain.usecase.TodoUseCase

class BasketViewModel(
    private val categoryUseCase: CategoryUseCase,
    private val noteUseCase: NoteUseCase,
    private val todoUseCase: TodoUseCase,
    private val labelUseCase: LabelUseCase,
    private val reminderUseCase: ReminderUseCase,
    private val basketUseCase: BasketUseCase
): ViewModel() {

    private val _basketUiState = MutableStateFlow(BasketUiState())
    val basketUiState: StateFlow<BasketUiState> = _basketUiState

    val basketContentState: StateFlow<BasketContentState> = combine(
        categoryUseCase.getCategoryDetailsList(),
        noteUseCase.getNoteDetailsList(),
        todoUseCase.getTodos(),
        labelUseCase.getLabels(),
        reminderUseCase.getReminders()
    ) { categories, notes, todos, labels, reminders ->
        BasketContentState(
            categories = categories.filter { it.deleted },
            notes = notes.filter { it.deleted },
            todos = todos.filter { it.deleted },
            labels = labels.filter { it.deleted },
            reminders = reminders.filter { it.deleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BasketContentState()
    )

    fun onActions(basketScreenActions: BasketScreenActions) {
        when(basketScreenActions) {
            is BasketScreenActions.RestoreFromBasketCategory -> {
                flintAction {
                    basketUseCase.restoreFromBasketCategory(basketScreenActions.categoryDetails)
                }
            }
            is BasketScreenActions.RestoreFromBasketNote -> {
                flintAction {
                    basketUseCase.restoreFromBasketNote(basketScreenActions.noteDetails)
                }
            }
            is BasketScreenActions.RestoreFromBasketTodo -> {
                flintAction {
                    basketUseCase.restoreFromBasketTodo(basketScreenActions.todoDetails)
                }
            }
            is BasketScreenActions.RestoreFromBasketLabel -> {
                flintAction {
                    basketUseCase.restoreFromBasketLabel(basketScreenActions.labelDetails)
                }
            }
            is BasketScreenActions.RestoreFromBasketReminders -> {
                flintAction {
                    basketUseCase.restoreFromBasketReminder(basketScreenActions.reminderDetails)
                }
            }
        }
    }

    private fun flintAction(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _basketUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _basketUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _basketUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}