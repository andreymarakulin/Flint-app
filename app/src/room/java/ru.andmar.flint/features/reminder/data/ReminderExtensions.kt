package ru.andmar.flint.features.reminder.model

import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.room.ReminderItem


fun ReminderDetails.toReminderItem(): ReminderItem = ReminderItem(
    id = id,
    noteId = noteId,
    title = title,
    reminderDate = reminderDate
)
fun ReminderItem.toReminderDetails(): ReminderDetails = ReminderDetails(
    id = id,
    noteId = noteId,
    title = title,
    reminderDate = reminderDate
)