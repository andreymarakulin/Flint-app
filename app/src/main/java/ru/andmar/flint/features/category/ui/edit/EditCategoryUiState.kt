package ru.andmar.flint.features.category.ui.edit

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.CategoryDetails

data class EditCategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false
)
