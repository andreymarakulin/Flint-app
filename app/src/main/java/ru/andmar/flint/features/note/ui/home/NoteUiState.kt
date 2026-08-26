package ru.andmar.flint.features.note.ui.home

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails

data class NoteUiState(
    val selectedNoteDetails: NoteDetails = NoteDetails(),
    val selectedCategoryDetails: CategoryDetails = CategoryDetails(),
    val flintActions: FlintActions = FlintActions.Default
)

