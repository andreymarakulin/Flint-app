package com.andmar.flint.firebase

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