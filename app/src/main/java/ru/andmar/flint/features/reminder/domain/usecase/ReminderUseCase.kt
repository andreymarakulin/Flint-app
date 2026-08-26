package ru.andmar.flint.features.reminder.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.data.toReminderItem
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.ui.home.ReminderDetailsListState

class ReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val flintAlarmScheduler: AlarmScheduler
) {

    fun getReminders(): Flow<List<ReminderDetails>> {
        return reminderRepository.getReminders()
    }

    suspend fun deleteReminder(reminderDetails: ReminderDetails) {
        flintAlarmScheduler.cancel(reminderDetails)
    }

}