package ru.andmar.flint.features.reminder.ui.entry

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

data class EntryReminderUiState(
    val reminderDetails: ReminderDetails = ReminderDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)