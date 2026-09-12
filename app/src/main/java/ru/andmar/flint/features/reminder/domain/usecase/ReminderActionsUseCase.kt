package ru.andmar.flint.features.reminder.domain.usecase

import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.domain.model.TodoDetails

class ReminderActionsUseCase(private val reminderRepository: ReminderRepository) {

    suspend fun fixReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(
            reminderDetails.copy(fix = !reminderDetails.fix)
        )
    }

    suspend fun doneReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(
            reminderDetails.copy(done = !reminderDetails.done)
        )
    }

    suspend fun highlightReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(
            reminderDetails.copy(highlight = !reminderDetails.highlight)
        )
    }

    suspend fun archiveReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(
            reminderDetails.copy(archive = true)
        )
    }

    suspend fun updateReminderDeleteState(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(
            reminderDetails.copy(deleted = !reminderDetails.deleted)
        )
    }

    suspend fun deleteReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.deleteReminder(reminderDetails)
    }
}