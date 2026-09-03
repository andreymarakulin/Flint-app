package ru.andmar.flint.features.category.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.random.Random

@Entity(tableName = "category_item")
data class CategoryItem(
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
    val createTime: Long,
    val updateTime: Long
)