package ru.andmar.flint.features.category.ui.entry

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.CategoryDetails

data class EntryCategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)
