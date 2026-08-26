package ru.andmar.flint.features.category.ui.home

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.ui.components.CategoryAction

data class CategoryUiState(
    val selectedCategoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default
)