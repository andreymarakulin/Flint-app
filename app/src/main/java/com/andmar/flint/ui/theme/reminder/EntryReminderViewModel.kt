package com.andmar.flint.ui.theme.reminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository

class EntryReminderViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var entryReminderUiState by mutableStateOf(EntryReminderUiState())
        private set

    fun onActions(entryReminderActions: EntryReminderActions) {
        when(entryReminderActions) {
            is EntryReminderActions.UpdateReminderDetails -> {
                updateReminderDetails(entryReminderActions.reminderDetails)
            }
            is EntryReminderActions.CreateReminder -> { createReminder() }
            is EntryReminderActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateReminderDetails(reminderDetails: ReminderDetails) {
        entryReminderUiState = EntryReminderUiState(
            reminderDetails = reminderDetails,
            isAction = isReminderAction(reminderDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        entryReminderUiState = entryReminderUiState.copy(
            flintActions = flintActions
        )
    }

    private fun createReminder() {

    }
}

data class EntryReminderUiState(
    val reminderDetails: ReminderDetails = ReminderDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EntryReminderActions {
    data class UpdateReminderDetails(val reminderDetails: ReminderDetails): EntryReminderActions
    object CreateReminder: EntryReminderActions
    object DismissError: EntryReminderActions
}

data class ReminderDetails(
    val id: String = "",
    val title: String = ""
)

fun isReminderAction(reminderDetails: ReminderDetails): Boolean {
    return true
}