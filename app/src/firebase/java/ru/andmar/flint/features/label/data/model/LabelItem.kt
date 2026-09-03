package ru.andmar.flint.features.label.data.model

data class LabelItem(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val color: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val deleted: Boolean = false,
    val archive: Boolean = false,
    val createTime: Long = 0L,
    val updateTime: Long = 0L
)