package ru.andmar.flint.features.note.domain.model



fun isNoteAction(noteDetails: NoteDetails): Boolean {
    return with(noteDetails) {
        title.isNotBlank() || text.isNotBlank()
    }
}