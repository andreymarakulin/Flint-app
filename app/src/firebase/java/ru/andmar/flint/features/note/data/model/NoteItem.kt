package ru.andmar.flint.features.note.data.model

data class NoteItem(
    val id: String = "",
    val categoryId: String = "",
    val reminderId: String = "",
    val labelId: String = "",
    val title: String = "",
    val text: String = "",
    val color: String = "Default",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val archive: Boolean = false,
    val createTime: Long = 0,
    val updateTime: Long = 0
)