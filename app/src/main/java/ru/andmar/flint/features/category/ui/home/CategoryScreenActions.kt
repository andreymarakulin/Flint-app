package ru.andmar.flint.features.category.ui.home

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.ui.components.CategoryAction

sealed interface CategoryScreenActions {
    data class UpdateSelectedCategoryDetails(val categoryDetails: CategoryDetails): CategoryScreenActions
    data class CategoryActions(val categoryAction: CategoryAction): CategoryScreenActions
    object DismissError: CategoryScreenActions
}