package ru.andmar.flint.features.note.ui.details

import ru.andmar.flint.features.note.domain.model.NoteDetails

sealed interface DetailsUiAction {
    object None: DetailsUiAction
    data class EditNote(val noteId: String): DetailsUiAction
    data class ShowDeleteSnackbar(val noteDetails: NoteDetails): DetailsUiAction
}