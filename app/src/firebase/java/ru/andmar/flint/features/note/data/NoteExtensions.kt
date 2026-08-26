package ru.andmar.flint.features.note.data

import ru.andmar.flint.features.note.data.model.NoteItem
import ru.andmar.flint.features.note.domain.model.NoteDetails

fun NoteDetails.toNoteItem(): NoteItem = NoteItem(
    id = id,
    categoryId = categoryId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)

fun NoteItem.toNoteDetails(): NoteDetails = NoteDetails(
    id = id,
    categoryId = categoryId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = updateTime
)