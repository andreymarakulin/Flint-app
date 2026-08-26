package ru.andmar.flint.features.note.domain.model

import androidx.compose.runtime.Immutable
import kotlin.random.Random

@Immutable
data class NoteDetails(
    val id: String = "",
    val categoryId: String = "",
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