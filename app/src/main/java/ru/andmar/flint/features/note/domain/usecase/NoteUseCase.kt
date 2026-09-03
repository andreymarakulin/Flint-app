package ru.andmar.flint.features.note.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.note.domain.model.NoteDetails

class NoteUseCase(private val noteRepository: NoteRepository) {

    fun getNoteDetailsList(): Flow<List<NoteDetails>> = noteRepository.getNotes()
}