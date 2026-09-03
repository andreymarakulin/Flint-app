package ru.andmar.flint.features.reminder.ui.entry

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

sealed interface EntryReminderScreenActions {
    data class UpdateReminderScreenDetails(val reminderDetails: ReminderDetails): EntryReminderScreenActions
    object CreateReminderScreen: EntryReminderScreenActions
    object DismissError: EntryReminderScreenActions
}
