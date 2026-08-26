package ru.andmar.flint.features.todo.ui.components

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
import ru.andmar.flint.features.reminder.domain.model.dateToUi
import ru.andmar.flint.features.todo.domain.model.TodoDetails


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoActionsSheet(
    sheetState: SheetState,
    todoDetails: TodoDetails,
    onActions: (TodoAction) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Column {
            if (todoDetails.title.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = todoDetails.title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (todoDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
            }
            if (todoDetails.text.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = todoDetails.text,
                        fontSize = 12.sp,
                        maxLines = 7,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (todoDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
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
                            text = "Обновлено: ${dateToUi(todoDetails.updateTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "Создано: ${dateToUi(todoDetails.createTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    if (todoDetails.done) {
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
                    if (todoDetails.highlight) {
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
                    if (todoDetails.fix) {
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
            todoActionsSheetItems(todoDetails) {
                onActions(it)
            }.forEach { item ->
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

fun todoActionsSheetItems(
    todoDetails: TodoDetails,
    onActions: (TodoAction) -> Unit
): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = if (todoDetails.fix) {
            R.string.unfix_title
        } else R.string.fix_title,
        icon = if (todoDetails.fix) {
            R.drawable.keep_off
        } else R.drawable.keep,
        description = null
    ) {
        onActions(TodoAction.FixTodo(todoDetails))
    },
    ModalSheetItem(
        title = if (todoDetails.done) {
            R.string.undone_title
        } else R.string.done_title,
        icon = if (todoDetails.done) {
            R.drawable.remove_done
        } else R.drawable.done_all,
        description = null
    ) {
        onActions(TodoAction.DoneTodo(todoDetails))
    },
    ModalSheetItem(
        title = if (todoDetails.highlight) {
            R.string.unhighlight_title
        } else R.string.highlight_title,
        icon = if (todoDetails.highlight) {
            R.drawable.heart_broken
        } else R.drawable.favorite,
        description = null
    ) {
        onActions(TodoAction.HighlightTodo(todoDetails))
    },
    ModalSheetItem(
        title = R.string.edit_title,
        icon = R.drawable.edit,
        description = null
    ) {
        onActions(TodoAction.EditTodo(todoDetails.id))
    },
    ModalSheetItem(
        title = R.string.delete_title,
        icon = R.drawable.delete,
        description = null
    ) {
        onActions(TodoAction.DeleteTodo(todoDetails))
    }
)