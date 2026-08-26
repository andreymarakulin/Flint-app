package ru.andmar.flint.features.category.ui.edit

import ru.andmar.flint.features.category.domain.model.CategoryDetails

sealed interface EditCategoryScreenActions {
    data class UpdateCategoryDetails(val categoryDetails: CategoryDetails): EditCategoryScreenActions
    object EditCategory: EditCategoryScreenActions
    object DismissError: EditCategoryScreenActions
}