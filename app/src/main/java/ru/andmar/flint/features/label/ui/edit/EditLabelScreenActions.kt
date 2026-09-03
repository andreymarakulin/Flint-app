package ru.andmar.flint.features.label.ui.edit

import ru.andmar.flint.features.label.domain.model.LabelDetails

sealed interface EditLabelScreenActions {
    data class UpdateLabelScreenDetails(val labelDetails: LabelDetails): EditLabelScreenActions
    object EditLabel: EditLabelScreenActions
    object DismissError: EditLabelScreenActions
}