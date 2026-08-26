package ru.andmar.flint.features.todo.domain.model

import kotlin.random.Random

data class TodoDetails(
    val id: String = "",
    val noteId: String = "",
    val reminderId: String = "",
    val labelId: String = "",
    val title: String = "",
    val text: String = "",
    val fix: Boolean = false,
    val done: Boolean = false,
    val highlight: Boolean = false,
    val deleted: Boolean = false,
    val createTime: Long = 0L,
    val updateTime: Long = 0L,
    val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
)