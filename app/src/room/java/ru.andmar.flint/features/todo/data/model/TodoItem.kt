package ru.andmar.flint.features.todo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

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