package ru.andmar.flint.features.note.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.data.toNoteDetails
import ru.andmar.flint.features.note.data.toNoteItem

class NoteRepository(private val flintDao: FlintDao) {

    suspend fun createNote(noteDetails: NoteDetails) = flintDao.insertNoteItem(noteDetails.toNoteItem())

    suspend fun editNote(noteDetails: NoteDetails) = flintDao.updateNoteItem(noteDetails.toNoteItem())

    suspend fun deleteNote(noteDetails: NoteDetails) = flintDao.deleteNoteItem(noteDetails.toNoteItem())

    suspend fun getNoteByIdOnce(noteId: String): NoteDetails = flintDao.getNoteItemById(noteId)
        .map { noteItem -> noteItem.toNoteDetails() }.first()
    fun getNoteById(noteId: String): Flow<NoteDetails> = flintDao.getNoteItemById(noteId)
        .map { noteItem -> noteItem.toNoteDetails() }

    fun getNotes(): Flow<List<NoteDetails>> = flintDao.getNoteItems().map { noteItems ->
        noteItems.map { noteItem -> noteItem.toNoteDetails() }
    }
}