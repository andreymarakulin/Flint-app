package ru.andmar.flint.features.note.data.model

data class NoteItem(
    val id: String = "",
    val categoryId: String = "",
    val labelId: String = "",
    val reminderId: String = "",
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