package ru.andmar.flint.features.label.ui.home

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.ui.components.LabelAction

sealed interface LabelScreenActions {
    data class UpdateSelectedLabelDetails(val labelDetails: LabelDetails): LabelScreenActions
    data class LabelActions(val labelAction: LabelAction): LabelScreenActions
    object DismissError: LabelScreenActions
}