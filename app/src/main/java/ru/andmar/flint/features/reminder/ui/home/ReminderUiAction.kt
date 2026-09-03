package ru.andmar.flint.features.reminder.ui.home

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

sealed interface ReminderUiAction {
    object None: ReminderUiAction
    data class EditReminder(val reminderId: String): ReminderUiAction
    data class ShowDeleteSnackbar(val reminderDetails: ReminderDetails): ReminderUiAction
}