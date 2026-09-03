package ru.andmar.flint.features.reminder.domain.usecase

import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

class EditReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val flintAlarmScheduler: AlarmScheduler
) {

    suspend fun getReminderById(reminderId: String): ReminderDetails {
        return reminderRepository.getReminderByIdOnce(reminderId)
    }

    suspend fun editReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.editReminder(reminderDetails)
        flintAlarmScheduler.cancel(reminderDetails)
        flintAlarmScheduler.scheduler(reminderDetails)
    }
}