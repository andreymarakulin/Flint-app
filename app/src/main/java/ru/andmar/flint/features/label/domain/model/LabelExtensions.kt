package ru.andmar.flint.features.label.domain.model

fun isLabelAction(labelDetails: LabelDetails): Boolean {
    return labelDetails.title.isNotBlank()
}