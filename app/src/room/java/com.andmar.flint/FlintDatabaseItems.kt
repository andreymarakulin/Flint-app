package com.andmar.flint.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andmar.flint.ui.theme.account.AuthDetails
import com.andmar.flint.ui.theme.category.CategoryDetails
import com.andmar.flint.ui.theme.label.LabelDetails
import com.andmar.flint.ui.theme.note.NoteDetails
import com.andmar.flint.ui.theme.reminder.ReminderDetails
import com.andmar.flint.ui.theme.todo.TodoDetails
import java.util.UUID


@Entity(tableName = "category_item")
data class CategoryItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val fix: Boolean,
    val highlight: Boolean,
    val updateTime: Long
)

@Entity(tableName = "note_item")
data class NoteItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val categoryId: String,
    val title: String,
    val text: String,
    val fix: Boolean,
    val done: Boolean,
    val highlight: Boolean,
    val updateTime: Long
)

@Entity(tableName = "todo_item")
data class TodoItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val noteId: String,
    val title: String,
    val text: String,
    val fix: Boolean,
    val done: Boolean,
    val highlight: Boolean,
    val updateTime: Long
)

@Entity(tableName = "label_item")
data class LabelItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)

@Entity(tableName = "reminder_item")
data class ReminderItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)

//Заглушки

data class AuthItem(
    val email: String = "",
    val password: String = ""
)

data class UserItem(
    val uid: String = ""
)


fun AuthDetails.toAuthItem(): AuthItem = AuthItem(
    email = email,
    password = password
)

fun AuthItem.toAuthDetails(): AuthDetails = AuthDetails(
    email = email,
    password = password
)

fun CategoryDetails.toCreateCategoryItem(): CategoryItem = CategoryItem(
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)

fun CategoryDetails.toCategoryItem(): CategoryItem = CategoryItem(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)

fun CategoryItem.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = updateTime
)

fun NoteDetails.toCreateNoteItem(): NoteItem = NoteItem(
    categoryId = categoryId,
    title = title,
    text = text,
    fix = fix,
    done = done,
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

fun TodoDetails.toCreateTodoItem(): TodoItem = TodoItem(
    noteId = noteId,
    title = title,
    text = text,
    fix = fix,
    done = done,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
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
fun LabelDetails.toLabelItem(): LabelItem = LabelItem(

)

fun LabelItem.toLabelDetails(): LabelDetails = LabelDetails(
    id = id
)

fun ReminderDetails.toReminderItem(): ReminderItem = ReminderItem(

)

fun ReminderItem.toReminderDetails(): ReminderDetails = ReminderDetails(
    id = id
)