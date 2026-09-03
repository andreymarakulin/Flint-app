package ru.andmar.flint.features.note.ui.entry

import ru.andmar.flint.features.note.domain.model.NoteDetails

sealed interface EntryNoteScreenActions {
    data class UpdateNoteScreenDetails(val noteDetails: NoteDetails): EntryNoteScreenActions
    object CreateNoteScreen: EntryNoteScreenActions
    object DismissError: EntryNoteScreenActions
}