package ru.andmar.flint.features.note.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

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