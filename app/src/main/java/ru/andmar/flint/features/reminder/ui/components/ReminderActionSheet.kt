package ru.andmar.flint.features.reminder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultModalSheetItem
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.domain.model.dateToUi
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderActionSheet(
    sheetState: SheetState,
    reminderDetails: ReminderDetails,
    onActions: (ReminderAction) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Column {
            if (reminderDetails.title.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = reminderDetails.title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (reminderDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
            }
            LazyRow() {
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "Обновлено: ${dateToUi(reminderDetails.updateTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "Создано: ${dateToUi(reminderDetails.createTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    if (reminderDetails.done) {
                        Card(Modifier.padding(3.dp)) {
                            Text(
                                text = "Выполненно",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
                item {
                    if (reminderDetails.highlight) {
                        Card(Modifier.padding(3.dp)) {
                            Text(
                                text = "Выделено",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
                item {
                    if (reminderDetails.fix) {
                        Card(Modifier.padding(3.dp)) {
                            Text(
                                text = "Закреплено",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
            Text(
                text = "Действия",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(horizontal = 10.dp)
            )
            reminderActionsSheetItems(
                reminderDetails = reminderDetails
            ) { onActions(it) }.forEach { item ->
                DefaultModalSheetItem(
                    title = item.title,
                    icon = item.icon,
                    description = item.description
                ) {
                    item.onClick()
                    onDismiss()
                }
            }
        }
    }
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