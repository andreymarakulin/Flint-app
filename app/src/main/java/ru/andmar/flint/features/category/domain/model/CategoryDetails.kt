package ru.andmar.flint.features.category.domain.model

import kotlin.random.Random

data class CategoryDetails(
    val id: String = "",
    val title: String = "",
    val fix: Boolean = false,
    val highlight: Boolean = false,
    val deleted: Boolean = false,
    val createTime: Long = 0L,
    val updateTime: Long = 0L,
    val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
)