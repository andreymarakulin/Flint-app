package ru.andmar.flint.features.category.domain.model

import ru.andmar.flint.core.ui.components.DefaultDetails
import kotlin.random.Random

data class CategoryDetails(
    override val id: String = "",
    override val title: String = "",
    override val fix: Boolean = false,
    override val highlight: Boolean = false,
    override val deleted: Boolean = false,
    override val createTime: Long = 0L,
    override val updateTime: Long = 0L,
    override val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
): DefaultDetails