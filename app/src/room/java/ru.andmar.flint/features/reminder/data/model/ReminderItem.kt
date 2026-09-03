package ru.andmar.flint.features.reminder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reminder_item")
data class ReminderItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val text: String,
    val color: String,
    val fix: Boolean,
    val done: Boolean,
    val highlight: Boolean,
    val deleted: Boolean,
    val archive: Boolean,
    val devicesName: String,
    val reminderDate: Long,
    val repeatInterval: Long,
    val createTime: Long,
    val updateTime: Long
)