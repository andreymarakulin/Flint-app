package ru.andmar.flint.features.note.domain.usecase

import kotlinx.coroutines.flow.first
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.note.domain.model.NoteDetails

class EditNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun getNoteById(noteId: String): NoteDetails {
        return noteRepository.getNoteByIdOnce(noteId)
    }

    suspend fun editNote(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(noteDetails)
    }
}