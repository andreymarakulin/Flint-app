package ru.andmar.flint.features.note.domain.usecase

import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.data.repository.NoteRepository
import ru.andmar.flint.features.note.domain.model.NoteDetails

class NoteActionsUseCase(private val noteRepository: NoteRepository) {

    suspend fun updateDeleteNoteState(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(deleted = !noteDetails.deleted)
        )
    }
    suspend fun fixNote(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(fix = !noteDetails.fix)
        )
    }

    suspend fun doneNote(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(done = !noteDetails.done)
        )
    }

    suspend fun highlightNote(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(highlight = !noteDetails.highlight)
        )
    }
    suspend fun archiveNote(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(archive = true)
        )
    }

    suspend fun editLabel(noteDetails: NoteDetails, labelDetails: LabelDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(labelDetails = labelDetails)
        )
    }

    suspend fun deleteLabel(noteDetails: NoteDetails) = runCatching {
        noteRepository.editNote(
            noteDetails.copy(labelDetails = LabelDetails())
        )
    }

    suspend fun deleteNote(noteDetails: NoteDetails) = runCatching() {
        noteRepository.deleteNote(noteDetails)
    }
}