package ru.andmar.flint.features.reminder.data

import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

fun ReminderDetails.toReminderItem(): ReminderItem = ReminderItem(
    id = id,
    title = title,
    text = text,
    color = color,
    fix = fix,
    done = done,
    highlight = highlight,
    deleted = deleted,
    archive = archive,
    devicesName = devicesName,
    reminderDate = reminderDate,
    repeatInterval = repeatInterval,
    createTime = createTime,
    updateTime = System.currentTimeMillis()
)

fun ReminderItem.toReminderDetails(): ReminderDetails = ReminderDetails(
    id = id,
    title = title,
    text = text,
    color = color,
    fix = fix,
    done = done,
    highlight = highlight,
    deleted = deleted,
    archive = archive,
    devicesName = devicesName,
    reminderDate = reminderDate,
    repeatInterval = repeatInterval,
    createTime = createTime,
    updateTime = updateTime
)