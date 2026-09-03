package ru.andmar.flint.features.reminder.data.model

data class ReminderItem(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val color: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val deleted: Boolean = false,
    val archive: Boolean = false,
    val devicesName: List<String> = emptyList(),
    val reminderDate: Long = 0L,
    val repeatInterval: Long = 0L,
    val createTime: Long = 0L,
    val updateTime: Long = 0L
)