package ru.andmar.flint.features.reminder.ui.home

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
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.domain.model.isReminderAction
import ru.andmar.flint.features.reminder.domain.usecase.ReminderActionsUseCase
import ru.andmar.flint.features.reminder.domain.usecase.ReminderUseCase
import ru.andmar.flint.features.reminder.ui.components.ReminderAction
import ru.andmar.flint.features.todo.ui.home.TodoUiAction

class ReminderViewModel(
    private val reminderUseCase: ReminderUseCase,
    private val reminderActionsUseCase: ReminderActionsUseCase
): ViewModel() {

    private val _reminderUiState = MutableStateFlow(ReminderUiState())
    val reminderUiState: StateFlow<ReminderUiState> = _reminderUiState
    private val _reminderUiAction = Channel<ReminderUiAction>()
    val reminderUiAction = _reminderUiAction.receiveAsFlow()

    val reminderDetailsListState: StateFlow<ReminderDetailsListState> =
        reminderUseCase.getReminders().map { reminderDetails ->
            ReminderDetailsListState(
                reminderDetails.filter{ !it.deleted }.sortedWith(
                    compareByDescending<ReminderDetails> { it.fix }
                        .thenByDescending { it.updateTime }
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReminderDetailsListState()
        )

    fun onActions(reminderScreenActions: ReminderScreenActions) {
        when(reminderScreenActions) {
            is ReminderScreenActions.UpdateSelectedReminderDetails -> {
                _reminderUiState.update {
                    it.copy(selelctedReminderDetails = reminderScreenActions.reminderDetails)
                }
            }
            is ReminderScreenActions.ReminderActions -> {
                when(val reminderAction = reminderScreenActions.reminderAction) {
                    is ReminderAction.FixReminder -> {

                    }
                    is ReminderAction.DoneReminder -> {
                        flintActions {
                            reminderActionsUseCase.fixReminder(reminderAction.reminderDetails)
                        }
                    }
                    is ReminderAction.HighlightReminder -> {
                        flintActions {
                            reminderActionsUseCase.highlightReminder(reminderAction.reminderDetails)
                        }
                    }
                    is ReminderAction.EditReminder -> {
                        viewModelScope.launch {
                            _reminderUiAction.send(
                                ReminderUiAction.EditReminder(
                                    reminderAction.reminderId
                                )
                            )
                        }
                    }
                    is ReminderAction.DeleteReminder -> {
                        flintActions {
                            reminderActionsUseCase.updateReminderDeleteState(reminderAction.reminderDetails)
                        }
                        viewModelScope.launch {
                            _reminderUiAction.send(
                                ReminderUiAction
                                    .ShowDeleteSnackbar(reminderAction.reminderDetails)
                            )
                        }
                    }
                    is ReminderAction.RestoreReminder -> {
                        flintActions {
                            reminderActionsUseCase.updateReminderDeleteState(reminderAction.reminderDetails)
                        }
                    }
                }
            }
            is ReminderScreenActions.DismissError -> {
                _reminderUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _reminderUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _reminderUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _reminderUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}