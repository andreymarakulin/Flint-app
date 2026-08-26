package ru.andmar.flint.features.reminder.data.repository

import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.core.data.FlintDao

class ReminderRepository(private val flintDao: FlintDao) {

    fun createReminder(reminderItem: ReminderItem) {
        TODO("Not yet implemented")
    }

    fun editReminder(reminderItem: ReminderItem) {
        TODO("Not yet implemented")
    }

    fun getReminders(): Flow<List<ReminderItem>> {
        TODO("Not yet implemented")
    }
}