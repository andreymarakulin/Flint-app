package ru.andmar.flint.features.note.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.note.data.toNoteDetails
import ru.andmar.flint.features.note.data.toNoteItem
import ru.andmar.flint.features.note.domain.model.NoteDetails

class NoteRepository(private val firestoreClient: DefaultFirestoreClient) {

    suspend fun createNote(noteDetails: NoteDetails) = firestoreClient.setNoteItem(noteDetails.toNoteItem())
    suspend fun editNote(noteDetails: NoteDetails) = firestoreClient.setNoteItem(noteDetails.toNoteItem())
    suspend fun deleteNote(noteDetails: NoteDetails) = firestoreClient.deleteNoteItem(noteDetails.id)
    suspend fun getNoteByIdOnce(noteId: String): NoteDetails = firestoreClient.getNoteItemOnce(noteId).toNoteDetails()
    fun getNoteById(noteId: String): Flow<NoteDetails> =
        firestoreClient.getNoteItem(noteId).map { noteItem -> noteItem.toNoteDetails() }
    fun getNotes(): Flow<List<NoteDetails>> =
        firestoreClient.getNoteItems().map { noteItems ->
            Log.i("Note items", noteItems.toString())
            noteItems.map { noteItem -> noteItem.toNoteDetails() }
        }
}