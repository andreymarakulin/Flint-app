package ru.andmar.flint.features.todo.data.model

data class TodoItem(
    val id: String = "",
    val noteId: String = "",
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