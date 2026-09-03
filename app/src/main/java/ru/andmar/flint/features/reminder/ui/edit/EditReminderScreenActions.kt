package ru.andmar.flint.features.reminder.ui.edit

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

sealed interface EditReminderScreenActions {
    data class UpdateReminderScreenDetails(val reminderDetails: ReminderDetails): EditReminderScreenActions
    object EditReminderScreen: EditReminderScreenActions
    object DismissError: EditReminderScreenActions
}