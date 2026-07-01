package com.andmar.flint.ui.theme.reminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository

class EditReminderViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var editReminderUiState by mutableStateOf(EditReminderUiState())
        private set

    fun onActions(editReminderActions: EditReminderActions) {
        when(editReminderActions) {
            is EditReminderActions.UpdateReminderDetails -> {
                updateReminderDetails(editReminderActions.reminderDetails)
            }
            is EditReminderActions.EditReminder -> { editReminder() }
            is EditReminderActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateReminderDetails(reminderDetails: ReminderDetails) {
        editReminderUiState = EditReminderUiState(
            reminderDetails = reminderDetails,
            isAction = isReminderAction(reminderDetails)
        )
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        editReminderUiState = editReminderUiState.copy(
            flintActions = flintActions
        )
    }

    private fun editReminder() {

    }
}

data class EditReminderUiState(
    val reminderDetails: ReminderDetails = ReminderDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)

sealed interface EditReminderActions {
    data class UpdateReminderDetails(val reminderDetails: ReminderDetails): EditReminderActions
    object EditReminder: EditReminderActions
    object DismissError: EditReminderActions
}