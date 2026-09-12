package ru.andmar.flint.features.note.data

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.data.model.NoteItem
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

fun NoteDetails.toNoteItem(): NoteItem = NoteItem(
    id = id,
    categoryId = categoryId,
    labelId = this.labelDetails.id,
    reminderId = this.reminderDetails.id,
    title = title,
    text = text,
    color = color,
    fix = fix,
    done = done,
    highlight = highlight,
    deleted = deleted,
    archive = archive,
    createTime = createTime,
    updateTime = System.currentTimeMillis()
)

suspend fun NoteItem.toNoteDetails(
    getLabel: suspend (String) -> LabelDetails,
    getReminder: suspend (String) -> ReminderDetails
): NoteDetails = NoteDetails(
    id = id,
    categoryId = categoryId,
    labelDetails = getLabel(this.labelId),
    reminderDetails = getReminder(this.reminderId),
    title = title,
    text = text,
    color = color,
    fix = fix,
    done = done,
    highlight = highlight,
    deleted = deleted,
    archive = archive,
    createTime = createTime,
    updateTime = updateTime
)
