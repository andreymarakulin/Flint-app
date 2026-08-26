package ru.andmar.flint.features.category.data.model

data class CategoryItem(
    val id: String = "",
    val title: String = "",
    val color: String = "Default",
    val fix: Boolean = false,
    val highlight: Boolean = false,
    val archive: Boolean = false,
    val createTime: Long = 0,
    val updateTime: Long = 0
)