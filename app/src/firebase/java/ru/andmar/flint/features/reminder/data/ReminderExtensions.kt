package ru.andmar.flint.features.reminder.data

import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

fun ReminderDetails.toReminderItem(): ReminderItem = ReminderItem(
    id = id,
    reminderId = reminderId,
    title = title,
    fix = fix,
    done = done,
    highlight = highlight,
    devicesName = devicesName,
    reminderDate = reminderDate,
    updateTime = System.currentTimeMillis()
)

fun ReminderItem.toReminderDetails(): ReminderDetails = ReminderDetails(
    reminderId = reminderId,
    title = title,
    fix = fix,
    done = done,
    highlight = highlight,
    devicesName = devicesName,
    reminderDate = reminderDate,
    updateTime = updateTime
)