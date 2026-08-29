package ru.andmar.flint.features.todo.domain.model

import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

data class TodoDetails(
    override val id: String = "",
    val noteId: String = "",
    val reminderId: String = "",
    val labelId: String = "",
    override val title: String = "",
    val text: String = "",
    override val fix: Boolean = false,
    val done: Boolean = false,
    override val highlight: Boolean = false,
    override val deleted: Boolean = false,
    override val createTime: Long = 0L,
    override val updateTime: Long = 0L,
    override val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
): DefaultDetails