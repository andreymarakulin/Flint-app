package ru.andmar.flint.features.note.ui.components

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
import ru.andmar.flint.features.reminder.domain.model.dateToUi
import kotlin.collections.forEach


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteActionsSheet(
    sheetState: SheetState,
    noteDetails: NoteDetails,
    onActions: (NoteAction) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Column {
            if (noteDetails.title.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = noteDetails.title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (noteDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
            }
            if (noteDetails.text.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = noteDetails.text,
                        fontSize = 12.sp,
                        maxLines = 7,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            LazyRow() {
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "Обновлено: ${dateToUi(noteDetails.updateTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "Создано: ${dateToUi(noteDetails.createTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    if (noteDetails.done) {
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
                    if (noteDetails.highlight) {
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
                    if (noteDetails.fix) {
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
            noteActionsSheetItems(
                noteDetails = noteDetails
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