package ru.andmar.flint.features.label.domain.model

import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

data class LabelDetails(
    override val id: String = "",
    override val title: String = "",
    override val text: String = "",
    val color: String = "default",
    override val fix: Boolean = false,
    override val done: Boolean = false,
    override val highlight: Boolean = false,
    val deleted: Boolean = false,
    val archive: Boolean = false,
    override val createTime: Long = 0L,
    override val updateTime: Long = 0L,
    val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
): DefaultDetails