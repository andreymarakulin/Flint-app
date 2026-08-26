package ru.andmar.flint.features.reminder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reminder_item")
data class ReminderItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
    val noteId: String = "",
    val title: String = "",
    val reminderDate: Long = 0
)