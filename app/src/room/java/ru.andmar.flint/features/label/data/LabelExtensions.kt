package ru.andmar.flint.features.label.data

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.data.model.LabelItem

fun LabelDetails.toLabelItem(): LabelItem = LabelItem(
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

fun LabelItem.toLabelDetails(): LabelDetails = LabelDetails(
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