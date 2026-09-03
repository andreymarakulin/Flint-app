package ru.andmar.flint.features.todo.data

import ru.andmar.flint.features.todo.data.model.TodoItem
import ru.andmar.flint.features.todo.domain.model.TodoDetails

fun TodoDetails.toTodoItem(): TodoItem = TodoItem(
    id = id,
    noteId = noteId,
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


fun TodoItem.toTodoDetails(): TodoDetails = TodoDetails(
    id = id,
    noteId = noteId,
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
    updateTime = updateTime
)