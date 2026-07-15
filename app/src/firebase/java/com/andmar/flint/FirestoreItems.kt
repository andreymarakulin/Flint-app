package com.andmar.flint

import com.andmar.flint.ui.theme.account.AuthDetails
import com.andmar.flint.ui.theme.category.CategoryDetails
import com.andmar.flint.ui.theme.label.LabelDetails
import com.andmar.flint.ui.theme.note.NoteDetails
import com.andmar.flint.ui.theme.reminder.ReminderDetails
import com.andmar.flint.ui.theme.todo.TodoDetails

const val APPS_COLLECTION = "apps"
const val APP_DOCUMENT = "Flint"
const val USER_COLLECTION = "Users"
const val CATEGORY_COLLECTION = "Categories"
const val NOTE_COLLECTION = "Notes"
const val TODO_COLLECTION = "Todos"
const val LABEL_COLLECTION = "Labels"
const val REMINDER_COLLECTION = "Reminder"

data class AuthItem(
    val email: String = "",
    val password: String = ""
)

data class UserItem(
    val uid: String = ""
)


data class CategoryItem(
    val id: String = "",
    val title: String = "",
    val fix: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)

data class NoteItem(
    val id: String = "",
    val categoryId: String = "",
    val title: String = "",
    val text: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)

data class TodoItem(
    val id: String = "",
    val noteId: String = "",
    val title: String = "",
    val text: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val updateTime: Long = 0
)

data class LabelItem(
    val id: String = ""
)

data class ReminderItem(
    val id: String = ""
)

fun AuthDetails.toAuthItem(): AuthItem = AuthItem(
    email = email,
    password = password
)

fun AuthItem.toAuthDetails(): AuthDetails = AuthDetails(
    email = email,
    password = password
)


fun CategoryItem.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = updateTime
)


fun CategoryDetails.toCategoryItem(): CategoryItem = CategoryItem(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)


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

fun LabelItem.toLabelDetails(): LabelDetails = LabelDetails(
    id = id
)


fun LabelDetails.toLabelItem(): LabelItem = LabelItem(
    id = id
)

fun ReminderItem.toReminderDetails(): ReminderDetails = ReminderDetails(
    id = id
)

fun ReminderDetails.toReminderItem(): ReminderItem = ReminderItem(
    id = id
)