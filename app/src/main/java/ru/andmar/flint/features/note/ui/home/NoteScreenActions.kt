package ru.andmar.flint.features.note.ui.home

import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.ui.components.CategoryAction
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.note.ui.home.NoteScreenActions

sealed interface NoteScreenActions {
    data class UpdateSelectedNoteDetails(val noteDetails: NoteDetails): NoteScreenActions
    data class UpdateSelectedCategoryDetails(val categoryDetails: CategoryDetails): NoteScreenActions
    data class NoteActions(val noteAction: NoteAction): NoteScreenActions
    data class EditLabel(val labelDetails: LabelDetails): NoteScreenActions
    data class CategoryActions(val categoryAction: CategoryAction): NoteScreenActions
    object DismissError: NoteScreenActions
}
