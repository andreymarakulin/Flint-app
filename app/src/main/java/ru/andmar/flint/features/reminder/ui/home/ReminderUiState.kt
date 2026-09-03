package ru.andmar.flint.features.reminder.ui.home

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.ui.components.ReminderAction

data class ReminderUiState(
    val selelctedReminderDetails: ReminderDetails = ReminderDetails(),
    val flintActions: FlintActions = FlintActions.Default
)