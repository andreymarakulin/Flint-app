package ru.andmar.flint.features.reminder.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.ActionsSheet
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderActionSheet(
    sheetState: SheetState,
    reminderDetails: ReminderDetails,
    onActions: (ReminderAction) -> Unit,
    onDismiss: () -> Unit
) {
    ActionsSheet(
        sheetState = sheetState,
        details = reminderDetails,
        actionsSheetItems = reminderActionsSheetItems(reminderDetails, onActions),
        onDismiss = onDismiss
    )
}

fun reminderActionsSheetItems(
    reminderDetails: ReminderDetails,
    onActions: (ReminderAction) -> Unit
): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = if (reminderDetails.fix) {
            R.string.unfix_title
        } else R.string.fix_title,
        icon = if (reminderDetails.fix) {
            R.drawable.keep_off
        } else R.drawable.keep,
        description = null
    ) { onActions(ReminderAction.FixReminder(reminderDetails)) },
    ModalSheetItem(
        title = if (reminderDetails.done) {
            R.string.undone_title
        } else R.string.done_title,
        icon = if (reminderDetails.done) {
            R.drawable.remove_done
        } else R.drawable.done_all,
        description = null
    ) { onActions(ReminderAction.DoneReminder(reminderDetails)) },
    ModalSheetItem(
        title = if (reminderDetails.highlight) {
            R.string.unhighlight_title
        } else R.string.highlight_title,
        icon = if (reminderDetails.highlight) {
            R.drawable.heart_broken
        } else R.drawable.favorite,
        description = null
    ) {onActions(ReminderAction.HighlightReminder(reminderDetails)) },
    ModalSheetItem(
        title = R.string.edit_title,
        icon = R.drawable.edit,
        description = null
    ) { onActions(ReminderAction.EditReminder(reminderDetails.id)) },
    ModalSheetItem(
        title = R.string.delete_title,
        icon = R.drawable.delete,
        description = null
    ) { onActions(ReminderAction.DeleteReminder(reminderDetails)) }
)