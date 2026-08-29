package ru.andmar.flint.core.ui.components

import kotlin.random.Random

interface DefaultDetails {
    val id: String
    val title: String
    val fix: Boolean
    val highlight: Boolean
    val deleted: Boolean
    val createTime: Long
    val updateTime: Long
    val lazyKey: Int
}