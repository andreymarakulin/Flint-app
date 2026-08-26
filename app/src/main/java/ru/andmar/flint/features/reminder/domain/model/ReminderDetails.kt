package ru.andmar.flint.features.reminder.domain.model

import kotlin.random.Random

data class ReminderDetails(
    val id: String = "",
    val reminderId: Int = 0,
    val title: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val deleted: Boolean = false,
    val devicesName: List<String> = emptyList(),
    val reminderDate: Long = 0, // Готовая дата для напоминания
    val updateTime: Long = 0L,
    val createTime: Long = 0L,
    // Праметры для ввода даты и времни
    val date: Long = System.currentTimeMillis(),
    val hours: Int = 0,
    val minutes: Int = 0,
    val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
)
