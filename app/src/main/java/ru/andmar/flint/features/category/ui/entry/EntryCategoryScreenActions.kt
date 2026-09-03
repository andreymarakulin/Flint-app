package ru.andmar.flint.features.category.ui.entry

import ru.andmar.flint.features.category.domain.model.CategoryDetails

sealed interface EntryCategoryScreenActions {
    data class UpdateCategoryScreenDetails(val categoryDetails: CategoryDetails): EntryCategoryScreenActions
    object CreateCategoryScreen: EntryCategoryScreenActions
    object DismissError: EntryCategoryScreenActions
}
