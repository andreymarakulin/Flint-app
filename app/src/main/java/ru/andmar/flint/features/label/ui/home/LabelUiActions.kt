package ru.andmar.flint.features.label.ui.home

import ru.andmar.flint.features.label.domain.model.LabelDetails

sealed interface LabelUiActions {
    object None: LabelUiActions
    data class EditLabel(val labelId: String): LabelUiActions
    data class ShowDeleteSnackbar(val labelDetails: LabelDetails): LabelUiActions
}