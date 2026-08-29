package ru.andmar.flint.features.todo.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.ActionsSheet
import ru.andmar.flint.features.todo.domain.model.TodoDetails


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoActionsSheet(
    sheetState: SheetState,
    todoDetails: TodoDetails,
    onActions: (TodoAction) -> Unit,
    onDismiss: () -> Unit
) {
    ActionsSheet(
        sheetState = sheetState,
        details = todoDetails,
        actionsSheetItems = todoActionsSheetItems(todoDetails, onActions),
        onDismiss = onDismiss
    )
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