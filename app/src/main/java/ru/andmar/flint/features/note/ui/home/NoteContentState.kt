package ru.andmar.flint.features.note.ui.home

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails

data class NoteContentState(
    val filteredCategoryDetailsList: List<CategoryDetails> = emptyList(),
    val filteredNoteDetailsListByCategory: Map<String, List<NoteDetails>> = emptyMap()
)
