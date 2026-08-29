package ru.andmar.flint.features.reminder.domain.model

import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

data class ReminderDetails(
    override val id: String = "",
    val reminderId: Int = 0,
    override val title: String = "",
    override val fix: Boolean = false,
    val done: Boolean = false,
    override val highlight: Boolean = false,
    override val deleted: Boolean = false,
    val devicesName: List<String> = emptyList(),
    val reminderDate: Long = 0, // Готовая дата для напоминания
    override val updateTime: Long = 0L,
    override val createTime: Long = 0L,
    // Праметры для ввода даты и времни
    val date: Long = System.currentTimeMillis(),
    val hours: Int = 0,
    val minutes: Int = 0,
    override val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
): DefaultDetails
