package ru.andmar.flint.features.todo.data

import ru.andmar.flint.features.todo.data.model.TodoItem
import ru.andmar.flint.features.todo.domain.model.TodoDetails

fun TodoDetails.toTodoItem(): TodoItem = TodoItem(
    id = id,
    noteId = noteId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)


fun TodoItem.toTodoDetails(): TodoDetails = TodoDetails(
    id = id,
    noteId = noteId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = updateTime
)