package ru.andmar.flint.features.reminder.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.reminder.data.scheduler.AlarmScheduler
import ru.andmar.flint.features.reminder.data.scheduler.FlintAlarmScheduler
import ru.andmar.flint.features.reminder.data.toReminderDetails
import ru.andmar.flint.features.reminder.data.toReminderItem
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

class ReminderRepository(
    private val firestoreClient: DefaultFirestoreClient
) {

    suspend fun createReminder(reminderDetails: ReminderDetails) {
        firestoreClient.setReminderItem(reminderDetails.toReminderItem())
    }
    suspend fun editReminder(reminderDetails: ReminderDetails) {
        firestoreClient.setReminderItem(reminderDetails.toReminderItem())
    }
    suspend fun deleteReminder(reminderDetails: ReminderDetails) {
        firestoreClient.deleteReminderItem(reminderDetails.id)
    }
    suspend fun getReminderByIdOnce(reminderId: String): ReminderDetails = firestoreClient.getReminderItemOnce(reminderId).toReminderDetails()
    fun getReminderById(reminderId: String): Flow<ReminderDetails> =
        firestoreClient.getReminderItem(reminderId).map { reminderItem -> reminderItem.toReminderDetails() }
    fun getReminders(): Flow<List<ReminderDetails>> =
        firestoreClient.getReminderItems().map { reminderItems ->
            reminderItems.map { reminderItem -> reminderItem.toReminderDetails() }
        }
}