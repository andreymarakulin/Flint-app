package ru.andmar.flint.features.label

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.model.LabelItem

fun LabelDetails.toLabelItem(): LabelItem = LabelItem(

)

fun LabelItem.toLabelDetails(): LabelDetails = LabelDetails(
    id = id
)