package ru.andmar.flint.features.reminder.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.reminder.data.toReminderDetails
import ru.andmar.flint.features.reminder.data.toReminderItem
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

class ReminderRepository(private val flintDao: FlintDao) {

    suspend fun createReminder(reminderDetails: ReminderDetails) = flintDao.insertReminderItem(reminderDetails.toReminderItem())

    suspend fun editReminder(reminderDetails: ReminderDetails) = flintDao.updateReminderItem(reminderDetails.toReminderItem())

    suspend fun deleteReminder(reminderDetails: ReminderDetails) = flintDao.deleteReminderItem(reminderDetails.toReminderItem())
    suspend fun getReminderByIdOnce(reminderId: String): ReminderDetails =
        flintDao.getReminderItemById(reminderId)
            .map { reminderItem -> reminderItem.toReminderDetails() }.first()

    suspend fun updateReminderDoneState(reminderId: String) {
        val reminderItem = flintDao.getReminderItemById(reminderId).first()

        flintDao.updateReminderItem(reminderItem.copy(done = true))
    }

    fun getReminderById(reminderId: String): Flow<ReminderDetails> =
        flintDao.getReminderItemById(reminderId)
            .map { reminderItem -> reminderItem.toReminderDetails() }

    fun getReminders(): Flow<List<ReminderDetails>> = flintDao.getReminderItems()
            .map { reminderItems ->
                reminderItems.map { reminderItem -> reminderItem.toReminderDetails() }
            }
}