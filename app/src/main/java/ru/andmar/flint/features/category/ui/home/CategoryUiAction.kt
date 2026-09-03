package ru.andmar.flint.features.category.ui.home

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails



sealed interface CategoryUiAction {
    object None: CategoryUiAction
    data class EditCategory(val categoryId: String): CategoryUiAction
    data class ShowDeleteSnackbar(val categoryDetails: CategoryDetails): CategoryUiAction
}