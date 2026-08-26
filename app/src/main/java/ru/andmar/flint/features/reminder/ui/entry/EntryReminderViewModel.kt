package ru.andmar.flint.features.reminder.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.reminder.domain.model.combineDateAndTime
import ru.andmar.flint.features.reminder.domain.model.isReminderAction
import ru.andmar.flint.features.reminder.domain.usecase.EntryReminderUseCase

class EntryReminderViewModel(
    private val entryReminderUseCase: EntryReminderUseCase
): ViewModel() {

    private val _entryReminderUiState = MutableStateFlow(EntryReminderUiState())
    val entryReminderUiState: StateFlow<EntryReminderUiState> = _entryReminderUiState

    fun onActions(entryReminderScreenActions: EntryReminderScreenActions) {
        when(entryReminderScreenActions) {
            is EntryReminderScreenActions.UpdateReminderScreenDetails -> {
                val reminderDetails = entryReminderScreenActions.reminderDetails
                val reminderDate = combineDateAndTime(
                    reminderDetails.date,
                    reminderDetails.hours,
                    reminderDetails.minutes
                )
                _entryReminderUiState.update {
                    it.copy(
                        reminderDetails = entryReminderScreenActions
                            .reminderDetails.copy(reminderDate = reminderDate),
                        isAction = isReminderAction(reminderDetails)
                    )
                }
            }
            is EntryReminderScreenActions.CreateReminderScreen -> {
                flintActions {
                    entryReminderUseCase.createReminder(_entryReminderUiState.value.reminderDetails)
                }
            }
            is EntryReminderScreenActions.DismissError -> {
                _entryReminderUiState.update {
                    it.copy(flintActions = FlintActions.Default)
                }
            }
        }
    }

   private fun flintActions(action: suspend () -> Result<Unit>) {
       viewModelScope.launch {
           _entryReminderUiState.update {
               it.copy(flintActions = FlintActions.Loading)
           }
           action().onSuccess {
               _entryReminderUiState.update {
                   it.copy(flintActions = FlintActions.Success)
               }
           }.onFailure { e ->
               _entryReminderUiState.update {
                   it.copy(flintActions = FlintActions.Error(e.message ?: "Error"))
               }
           }
       }
   }
}