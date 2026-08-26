package ru.andmar.flint.features.label.domain.model

import kotlin.random.Random

data class LabelDetails(
    val id: String = "",
    val title: String = "",
    val fix: Boolean = false,
    val highlight: Boolean = false,
    val deleted: Boolean = false,
    val updateTime: Long = 0,
    val lazyKey: Int = Random.nextInt(Int.MAX_VALUE)
)