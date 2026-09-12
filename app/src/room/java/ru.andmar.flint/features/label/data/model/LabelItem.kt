package ru.andmar.flint.features.label.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "label_item")
data class LabelItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val text: String,
    val color: String,
    val fix: Boolean,
    val done: Boolean,
    val highlight: Boolean,
    val deleted: Boolean,
    val archive: Boolean,
    val choice: Boolean,
    val createTime: Long,
    val updateTime: Long
)