package ru.andmar.flint.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ModalSheetItem(
    @StringRes
    val title: Int,
    @DrawableRes
    val icon: Int,
    val description: String? = null,
    val onClick: () -> Unit
)