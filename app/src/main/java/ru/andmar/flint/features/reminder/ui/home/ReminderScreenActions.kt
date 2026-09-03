package ru.andmar.flint.features.reminder.ui.home

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.ui.components.ReminderAction

sealed interface ReminderScreenActions {
    data class UpdateSelectedReminderDetails(val reminderDetails: ReminderDetails): ReminderScreenActions
    data class ReminderActions(val reminderAction: ReminderAction): ReminderScreenActions
    object DismissError: ReminderScreenActions
}