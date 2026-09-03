package ru.andmar.flint.features.note.data

import ru.andmar.flint.features.note.data.model.NoteItem
import ru.andmar.flint.features.note.domain.model.NoteDetails

fun NoteDetails.toNoteItem(): NoteItem = NoteItem(
    id = id,
    categoryId = categoryId,
    labelId = labelId,
    reminderId = reminderId,
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

fun NoteItem.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
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