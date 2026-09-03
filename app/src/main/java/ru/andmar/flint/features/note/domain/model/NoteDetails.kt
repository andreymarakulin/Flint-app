package ru.andmar.flint.features.note.domain.model

import androidx.compose.runtime.Immutable
import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

@Immutable
data class NoteDetails(
    override val id: String = "",
    val categoryId: String = "",
    val labelId: String = "",
    val reminderId: String = "",
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