package ru.andmar.flint.features.note.ui.edit

import ru.andmar.flint.features.note.domain.model.NoteDetails

sealed interface EditNoteScreenActions {
    data class UpdateNoteScreenDetails(val noteDetails: NoteDetails): EditNoteScreenActions
    data class NavBack(val onNavBack: () -> Unit): EditNoteScreenActions
    object EditNoteScreen: EditNoteScreenActions
    object DismissError: EditNoteScreenActions
}