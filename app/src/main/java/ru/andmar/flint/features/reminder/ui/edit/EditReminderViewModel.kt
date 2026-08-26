package ru.andmar.flint.features.reminder.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.reminder.domain.model.isReminderAction
import ru.andmar.flint.features.reminder.domain.usecase.EditReminderUseCase
import ru.andmar.flint.navigation.NavigationRoutes

class EditReminderViewModel(
    private val editReminderUseCase: EditReminderUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val reminderId: String = savedStateHandle.toRoute<NavigationRoutes.EditReminderScreenRoute>().reminderId

    private val _editReminderUiState = MutableStateFlow(EditReminderUiState())
    val editReminderUiState: StateFlow<EditReminderUiState> = _editReminderUiState

    init {
        viewModelScope.launch {
            _editReminderUiState.update {
                it.copy(reminderDetails = editReminderUseCase.getReminderById(reminderId))
            }
        }
    }

    fun onActions(editReminderScreenActions: EditReminderScreenActions) {
        when(editReminderScreenActions) {
            is EditReminderScreenActions.UpdateReminderScreenDetails -> {
                _editReminderUiState.update {
                    it.copy(
                        reminderDetails = editReminderScreenActions.reminderDetails,
                        isAction = isReminderAction(editReminderScreenActions.reminderDetails)
                    )
                }
            }
            is EditReminderScreenActions.EditReminderScreen -> {
                flintActions {
                    editReminderUseCase.editReminder(_editReminderUiState.value.reminderDetails)
                }
            }
            is EditReminderScreenActions.DismissError -> {
                _editReminderUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

    private fun flintActions(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _editReminderUiState.update {
                it.copy(flintActions = FlintActions.Loading)
            }
            action().onSuccess {
                _editReminderUiState.update {
                    it.copy(flintActions = FlintActions.Success)
                }
            }.onFailure { e ->
                _editReminderUiState.update {
                    it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
                }
            }
        }
    }
}