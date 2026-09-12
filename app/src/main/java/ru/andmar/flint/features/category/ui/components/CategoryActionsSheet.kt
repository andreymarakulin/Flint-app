package ru.andmar.flint.features.category.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.sheet.ActionsSheet
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.todo.ui.components.TodoAction


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryActionsSheet(
    sheetState: SheetState,
    categoryDetails: CategoryDetails,
    onActions: (CategoryAction) -> Unit,
    onDismiss: () -> Unit
) {
    ActionsSheet(
        sheetState = sheetState,
        details = categoryDetails,
        actionsSheetItems = categoryActionsSheetItems(categoryDetails, onActions),
        onDismiss = onDismiss
    )
}

fun categoryActionsSheetItems(
    categoryDetails: CategoryDetails,
    onActions: (CategoryAction) -> Unit
): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = if (categoryDetails.fix) {
            R.string.unfix_title
        } else R.string.fix_title,
        icon = if (categoryDetails.fix) {
            R.drawable.keep_off
        } else R.drawable.keep,
        description = null
    ) {
        onActions(CategoryAction.FixCategory(categoryDetails))
    },
    ModalSheetItem(
        title = if (categoryDetails.highlight) {
            R.string.unhighlight_title
        } else R.string.highlight_title,
        icon = if (categoryDetails.highlight) {
            R.drawable.heart_broken
        } else R.drawable.favorite,
        description = null
    ) {
        onActions(CategoryAction.HighlightCategory(categoryDetails))
    },
    ModalSheetItem(
        title = R.string.archive_title,
        icon = R.drawable.archive,
        description = null
    ) {onActions(CategoryAction.ArchiveCategory(categoryDetails)) },
    ModalSheetItem(
        title = R.string.edit_title,
        icon = R.drawable.edit,
        description = null
    ) {
        onActions(CategoryAction.EditCategory(categoryDetails.id))
    },
    ModalSheetItem(
        title = R.string.delete_title,
        icon = R.drawable.delete,
        description = null
    ) {
        onActions(CategoryAction.DeleteCategory(categoryDetails))
    }
)