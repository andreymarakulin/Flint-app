package com.andmar.flint.ui.theme.reminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andmar.flint.FlintActions
import com.andmar.flint.data.DefaultFlintRepository

class ReminderViewModel(
    private val flintRepository: DefaultFlintRepository
): ViewModel() {

    var reminderUiState by mutableStateOf(ReminderUiState())
        private set

    fun onActions(reminderActions: ReminderActions) {
        when(reminderActions) {
            is ReminderActions.DismissError -> {
                updateFlintActions(FlintActions.Default)
            }
        }
    }

    private fun updateFlintActions(flintActions: FlintActions) {
        reminderUiState = reminderUiState.copy(
            flintActions = flintActions
        )
    }
}

data class ReminderUiState(
    val selectedReminderDetails: ReminderDetails = ReminderDetails(),
    val flintActions: FlintActions = FlintActions.Default
)

sealed interface ReminderActions {
    object DismissError: ReminderActions
}