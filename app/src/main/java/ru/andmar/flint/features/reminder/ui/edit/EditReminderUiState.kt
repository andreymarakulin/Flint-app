package ru.andmar.flint.features.reminder.ui.edit

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

data class EditReminderUiState(
    val reminderDetails: ReminderDetails = ReminderDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)