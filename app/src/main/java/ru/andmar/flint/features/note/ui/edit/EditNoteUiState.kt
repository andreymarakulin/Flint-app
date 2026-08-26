package ru.andmar.flint.features.note.ui.edit

import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.note.domain.model.NoteDetails


data class EditNoteUiState(
    val noteDetails: NoteDetails = NoteDetails(),
    val flintActions: FlintActions = FlintActions.Default,
    val isAction: Boolean = false,
    val isEdit: Boolean = false
)

