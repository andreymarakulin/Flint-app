package ru.andmar.flint.features.category.data

import ru.andmar.flint.features.category.data.model.CategoryItem
import ru.andmar.flint.features.category.domain.model.CategoryDetails

fun CategoryDetails.toCategoryItem(): CategoryItem = CategoryItem(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = System.currentTimeMillis()
)

fun CategoryItem.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    title = title,
    fix = fix,
    highlight = highlight,
    updateTime = updateTime
)
