package ru.andmar.flint.features.reminder.ui.components

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

sealed interface ReminderAction {

    data class FixReminder(val reminderDetails: ReminderDetails) : ReminderAction
    data class DoneReminder(val reminderDetails: ReminderDetails) : ReminderAction
    data class HighlightReminder(val reminderDetails: ReminderDetails) : ReminderAction
    data class EditReminder(val reminderId: String) : ReminderAction
    data class DeleteReminder(val reminderDetails: ReminderDetails) : ReminderAction
    data class RestoreReminder(val reminderDetails: ReminderDetails) : ReminderAction
}