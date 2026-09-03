package ru.andmar.flint.features.note.domain.usecase

import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.note.domain.model.NoteDetails
import java.util.UUID

class EntryNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun createNote(noteDetails: NoteDetails, categoryId: String) = runCatching {
        noteRepository.createNote(
            noteDetails.copy(
                id = UUID.randomUUID().toString(),
                categoryId = categoryId,
                createTime = System.currentTimeMillis()
            )
        )
    }
}