package ru.andmar.flint.features.category.data

import ru.andmar.flint.features.category.data.model.CategoryItem
import ru.andmar.flint.features.category.domain.model.CategoryDetails

fun CategoryDetails.toCategoryItem(): CategoryItem = CategoryItem(
    id = id,
    title = title,
    text = text,
    color = color,
    fix = fix,
    done = done,
    highlight = highlight,
    deleted = deleted,
    archive = archive,
    createTime = createTime,
    updateTime = System.currentTimeMillis()
)

fun CategoryItem.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    title = title,
    text = text,
    color = color,
    fix = fix,
    done = done,
    highlight = highlight,
    deleted = deleted,
    archive = archive,
    createTime = createTime,
    updateTime = updateTime
)
