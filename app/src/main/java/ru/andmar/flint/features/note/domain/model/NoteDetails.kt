package ru.andmar.flint.features.note.domain.model

import androidx.compose.runtime.Immutable
import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

@Immutable
data class NoteDetails(
    override val id: String = "",
    val categoryId: String = "",
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