package ru.andmar.flint.features.note.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.label.data.toLabelDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.data.toNoteDetails
import ru.andmar.flint.features.note.data.toNoteItem
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.reminder.data.toReminderDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

class NoteRepository(private val firestoreClient: DefaultFirestoreClient) {

    suspend fun createNote(noteDetails: NoteDetails) = firestoreClient.setNoteItem(noteDetails.toNoteItem())
    suspend fun editNote(noteDetails: NoteDetails) = firestoreClient.setNoteItem(noteDetails.toNoteItem())
    suspend fun deleteNote(noteDetails: NoteDetails) = firestoreClient.deleteNoteItem(noteDetails.id)
    suspend fun getNoteByIdOnce(noteId: String): NoteDetails {
        val noteItem = firestoreClient.getNoteItemOnce(noteId)
        val labelItem: LabelItem? = if (noteItem.labelId.isNotBlank()) {
            firestoreClient.getLabelItemOnce(noteItem.labelId)
        } else null
        val reminderItem: ReminderItem? = if (noteItem.reminderId.isNotBlank()) {
            firestoreClient.getReminderItemOnce(noteItem.reminderId)
        } else null

        return noteItem.toNoteDetails(
            getLabel = { labelItem?.toLabelDetails() ?: LabelDetails() },
            getReminder = { reminderItem?.toReminderDetails() ?: ReminderDetails() }
        )
    }

    fun getNoteById(noteId: String): Flow<NoteDetails> {
        return combine(
            firestoreClient.getNoteItem(noteId),
            firestoreClient.getLabelItems(),
            firestoreClient.getReminderItems()
        ) { note, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            note.toNoteDetails(
                getLabel = { labelId -> labelsMap[labelId]?.toLabelDetails() ?: LabelDetails() },
                getReminder = { reminderId -> remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails () }
            )
        }
    }

    fun getNotes(): Flow<List<NoteDetails>> {
        return combine(
            firestoreClient.getNoteItems(),
            firestoreClient.getLabelItems(),
            firestoreClient.getReminderItems()
        ) { notes, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            notes.map { noteItem ->
                noteItem.toNoteDetails(
                    getLabel = { labelId ->
                        labelsMap[labelId]?.toLabelDetails() ?: LabelDetails()
                    },
                    getReminder = { reminderId ->
                        remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails()
                    }
                )
            }
        }
    }
}