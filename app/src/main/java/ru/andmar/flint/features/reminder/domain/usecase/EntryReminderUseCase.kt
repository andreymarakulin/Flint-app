package ru.andmar.flint.features.reminder.domain.usecase

import android.os.Build
import ru.andmar.flint.features.reminder.data.repository.ReminderRepository
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import java.util.UUID
import kotlin.random.Random

class EntryReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val flintAlarmScheduler: AlarmScheduler
) {

    suspend fun createReminder(reminderDetails: ReminderDetails) = runCatching {
        reminderRepository.createReminder(
            reminderDetails.copy(
                id = UUID.randomUUID().toString(),
                reminderId = Random.nextInt(Int.MAX_VALUE),
                devicesName = reminderDetails.devicesName + listOf("${Build.DEVICE} ${Build.MODEL}"),
                createTime = System.currentTimeMillis()
            )
        )
        flintAlarmScheduler.scheduler(reminderDetails)
    }
}