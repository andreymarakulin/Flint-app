package ru.andmar.flint.features.category.ui.components

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.ui.home.CategoryScreenActions
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction

interface CategoryAction {
    data class FixCategory(val categoryDetails: CategoryDetails): CategoryAction
    data class HighlightCategory(val categoryDetails: CategoryDetails): CategoryAction
    data class EditCategory(val categoryId: String): CategoryAction
    data class DeleteCategory(val categoryDetails: CategoryDetails): CategoryAction
    data class RestoreCategory(val categoryDetails: CategoryDetails): CategoryAction
}