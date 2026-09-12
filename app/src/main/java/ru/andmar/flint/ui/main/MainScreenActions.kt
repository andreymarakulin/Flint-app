package ru.andmar.flint.ui.main

import ru.andmar.flint.features.label.domain.model.LabelDetails

sealed interface MainScreenActions {
    data class RemoveChoiceLabelDetails(val labelDetails: LabelDetails): MainScreenActions
}