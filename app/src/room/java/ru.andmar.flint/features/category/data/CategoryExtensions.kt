package ru.andmar.flint.features.category

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.model.CategoryItem

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