package ru.andmar.flint.features.note.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.features.label.data.model.LabelItem
import ru.andmar.flint.features.label.data.toLabelDetails
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.data.toNoteDetails
import ru.andmar.flint.features.note.data.toNoteItem
import ru.andmar.flint.features.reminder.data.model.ReminderItem
import ru.andmar.flint.features.reminder.data.toReminderDetails
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

class NoteRepository(private val flintDao: FlintDao) {

    suspend fun createNote(noteDetails: NoteDetails) = flintDao.insertNoteItem(noteDetails.toNoteItem())

    suspend fun editNote(noteDetails: NoteDetails) = flintDao.updateNoteItem(noteDetails.toNoteItem())

    suspend fun deleteNote(noteDetails: NoteDetails) = flintDao.deleteNoteItem(noteDetails.toNoteItem())

    suspend fun getNoteByIdOnce(noteId: String): NoteDetails {
        val noteItem = flintDao.getNoteItemById(noteId).first()
        val labelItem: LabelItem? = if (noteItem.labelId.isNotBlank()) {
            flintDao.getLabelItemById(noteItem.labelId).first()
        } else null
        val reminderItem: ReminderItem? = if (noteItem.reminderId.isNotBlank()) {
            flintDao.getReminderItemById(noteItem.reminderId).first()
        } else null

        return noteItem.toNoteDetails(
            getLabel = { labelItem?.toLabelDetails() ?: LabelDetails() },
            getReminder = { reminderItem?.toReminderDetails() ?: ReminderDetails() }
        )
    }
    fun getNoteById(noteId: String): Flow<NoteDetails> {
        return combine(
            flintDao.getNoteItemById(noteId),
            flintDao.getLabelItems(),
            flintDao.getReminderItems()
        ) { note, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            note.toNoteDetails(
                getLabel = { labelId -> labelsMap[labelId]?.toLabelDetails() ?: LabelDetails() },
                getReminder = { reminderId -> remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails() }
            )
        }
    }

    fun getNotes(): Flow<List<NoteDetails>> {
        return combine(
            flintDao.getNoteItems(),
            flintDao.getLabelItems(),
            flintDao.getReminderItems()
        ) { notes, labels, reminders ->
            val labelsMap = labels.associateBy { it.id }
            val remindersMap = reminders.associateBy { it.id }

            notes.map { noteItem ->
                noteItem.toNoteDetails(
                    getLabel = { labelId -> labelsMap[labelId]?.toLabelDetails() ?: LabelDetails() },
                    getReminder = { reminderId -> remindersMap[reminderId]?.toReminderDetails() ?: ReminderDetails() }
                )
            }
        }
    }
}