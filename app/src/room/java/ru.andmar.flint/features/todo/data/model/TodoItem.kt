package ru.andmar.flint.features.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "todo_item")
data class TodoItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val noteId: String,
    val labelId: String,
    val reminderId: String,
    val title: String,
    val text: String,
    val color: String,
    val fix: Boolean,
    val done: Boolean,
    val highlight: Boolean,
    val deleted: Boolean,
    val archive: Boolean,
    val createTime: Long,
    val updateTime: Long
)