package ru.andmar.flint.features.todo.domain.model

import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

data class TodoDetails(
    override val id: String = "",
    val noteId: String = "",
    val reminderId: String = "",
    val labelId: String = "",
    override val title: String = "",
    override val text: String = "",
    val color: String = "",
    override val fix: Boolean = false,
    override val done: Boolean = false,
    override val highlight: Boolean = false,
    val deleted: Boolean = false,
    val archive: Boolean = false,
    override val createTime: Long = 0L,
    override val updateTime: Long = 0L,
    val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
): DefaultDetails