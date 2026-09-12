package ru.andmar.flint.features.todo.data

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.todo.data.model.TodoItem
import ru.andmar.flint.features.todo.domain.model.TodoDetails

fun TodoDetails.toTodoItem(): TodoItem = TodoItem(
    id = id,
    noteId = noteId,
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


suspend fun TodoItem.toTodoDetails(
    getLabel: suspend (String) -> LabelDetails,
    getReminder: suspend (String) -> ReminderDetails
): TodoDetails = TodoDetails(
    id = id,
    noteId = noteId,
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