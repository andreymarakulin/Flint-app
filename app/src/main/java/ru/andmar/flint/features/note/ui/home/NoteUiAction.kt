package ru.andmar.flint.features.note.ui.home

import ru.andmar.flint.features.note.domain.model.NoteDetails

sealed interface NoteUiAction {
    object None: NoteUiAction
    object ChoiceLabelSheet: NoteUiAction
    data class EditNote(val noteId: String): NoteUiAction
    data class ShowDeleteSnackbar(val noteDetails: NoteDetails): NoteUiAction
}