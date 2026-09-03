package ru.andmar.flint.features.label.ui.edit

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.domain.model.LabelDetails

data class EditLabelUiState(
    val labelDetails: LabelDetails = LabelDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)