package ru.andmar.flint.features.label.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.sheet.ActionsSheet
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelActionsSheet(
    sheetState: SheetState,
    labelDetails: LabelDetails,
    onActions: (LabelAction) -> Unit,
    onDismiss: () -> Unit
) {
    ActionsSheet(
        sheetState = sheetState,
        details = labelDetails,
        actionsSheetItems = labelActionsSheetItems(labelDetails, onActions),
        onDismiss = onDismiss
    )
}

fun labelActionsSheetItems(
    labelDetails: LabelDetails,
    onActions: (LabelAction) -> Unit
): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = if (labelDetails.fix) {
            R.string.unfix_title
        } else R.string.fix_title,
        icon = if (labelDetails.fix) {
            R.drawable.keep_off
        } else R.drawable.keep,
        description = null
    ) { onActions(LabelAction.FixLabel(labelDetails)) },
    ModalSheetItem(
        title = if (labelDetails.done) {
            R.string.undone_title
        } else R.string.done_title,
        icon = if (labelDetails.done) {
            R.drawable.remove_done
        } else R.drawable.done_all,
        description = null
    ) { onActions(LabelAction.DoneLabel(labelDetails)) },
    ModalSheetItem(
        title = if (labelDetails.highlight) {
            R.string.unhighlight_title
        } else R.string.highlight_title,
        icon = if (labelDetails.highlight) {
            R.drawable.heart_broken
        } else R.drawable.favorite,
        description = null
    ) {onActions(LabelAction.HighlightLabel(labelDetails)) },
    ModalSheetItem(
        title = R.string.archive_title,
        icon = R.drawable.archive,
        description = null
    ) {onActions(LabelAction.ArchiveLabel(labelDetails)) },
    ModalSheetItem(
        title = R.string.edit_title,
        icon = R.drawable.edit,
        description = null
    ) { onActions(LabelAction.EditLabel(labelDetails.id)) },
    ModalSheetItem(
        title = R.string.delete_title,
        icon = R.drawable.delete,
        description = null
    ) { onActions(LabelAction.DeleteLabel(labelDetails)) }
)