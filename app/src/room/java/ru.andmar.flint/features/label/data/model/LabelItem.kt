package ru.andmar.flint.features.label.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "label_item")
data class LabelItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
)