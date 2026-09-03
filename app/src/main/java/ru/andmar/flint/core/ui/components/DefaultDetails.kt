package ru.andmar.flint.core.ui.components

import kotlin.random.Random

interface DefaultDetails {
    val id: String
    val title: String
    val text: String
    val fix: Boolean
    val done: Boolean
    val highlight: Boolean
    val createTime: Long
    val updateTime: Long
}