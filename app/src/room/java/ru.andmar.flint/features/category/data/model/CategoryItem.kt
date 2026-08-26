package ru.andmar.flint.features.category.model

import androidx.room.Entity
import androidx.room.PrimaryKey
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