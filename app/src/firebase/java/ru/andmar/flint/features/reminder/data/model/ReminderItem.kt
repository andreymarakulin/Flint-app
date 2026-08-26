package ru.andmar.flint.features.reminder.data.model

data class ReminderItem(
    val id: String = "",
    val reminderId: Int = 0,
    val title: String = "",
    val color: String = "Default",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val repeat: Boolean = false,
    val devicesName: List<String> = emptyList(),
    val reminderDate: Long = 0, // Готовая дата для напоминания
    val repeatTime: Long = 0, //Время повторения (прибавить в reminderDate при создании напоминания)
    val createTime: Long = 0,
    val updateTime: Long = 0
)