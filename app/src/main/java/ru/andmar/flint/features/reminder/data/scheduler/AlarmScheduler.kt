package ru.andmar.flint.features.reminder.data.scheduler

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

interface AlarmScheduler {
    fun scheduler(item: ReminderDetails)
    fun cancel(item: ReminderDetails)
}