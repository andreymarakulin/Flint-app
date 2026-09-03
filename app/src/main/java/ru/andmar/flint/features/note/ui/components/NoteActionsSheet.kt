package ru.andmar.flint.features.note.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.ActionsSheet
import ru.andmar.flint.features.note.domain.model.NoteDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteActionsSheet(
    sheetState: SheetState,
    noteDetails: NoteDetails,
    onActions: (NoteAction) -> Unit,
    onDismiss: () -> Unit
) {
    ActionsSheet(
        sheetState = sheetState,
        details = noteDetails,
        actionsSheetItems = noteActionsSheetItems(noteDetails, onActions),
        onDismiss = onDismiss
    )
}

fun noteActionsSheetItems(
    noteDetails: NoteDetails,
    onActions: (NoteAction) -> Unit
): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = if (noteDetails.fix) {
            R.string.unfix_title
        } else R.string.fix_title,
        icon = if (noteDetails.fix) {
            R.drawable.keep_off
        } else R.drawable.keep,
        description = null
    ) { onActions(NoteAction.FixNote(noteDetails)) },
    ModalSheetItem(
        title = if (noteDetails.done) {
            R.string.undone_title
        } else R.string.done_title,
        icon = if (noteDetails.done) {
            R.drawable.remove_done
        } else R.drawable.done_all,
        description = null
    ) { onActions(NoteAction.DoneNote(noteDetails)) },
    ModalSheetItem(
        title = if (noteDetails.highlight) {
            R.string.unhighlight_title
        } else R.string.highlight_title,
        icon = if (noteDetails.highlight) {
            R.drawable.heart_broken
        } else R.drawable.favorite,
        description = null
    ) {onActions(NoteAction.HighlightNote(noteDetails)) },
    ModalSheetItem(
        title = R.string.edit_title,
        icon = R.drawable.edit,
        description = null
    ) { onActions(NoteAction.EditNote(noteDetails.id)) },
    ModalSheetItem(
        title = R.string.delete_title,
        icon = R.drawable.delete,
        description = null
    ) { onActions(NoteAction.DeleteNote(noteDetails)) }
)