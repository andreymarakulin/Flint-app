package ru.andmar.flint.features.label.ui.home

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.label.domain.model.LabelDetails

data class LabelUiState(
    val selectedLabelDetails: LabelDetails = LabelDetails(),
    val flintActions: FlintActions = FlintActions.Default
)