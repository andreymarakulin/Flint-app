package ru.andmar.flint.features.note.ui.components

import ru.andmar.flint.features.note.domain.model.NoteDetails

sealed interface NoteAction {
    data class FixNote(val noteDetails: NoteDetails) : NoteAction
    data class DoneNote(val noteDetails: NoteDetails) : NoteAction
    data class HighlightNote(val noteDetails: NoteDetails) : NoteAction
    data class EditNote(val noteId: String): NoteAction
    data class DeleteNote(val noteDetails: NoteDetails) : NoteAction
    data class RestoreNote(val noteDetails: NoteDetails): NoteAction
}