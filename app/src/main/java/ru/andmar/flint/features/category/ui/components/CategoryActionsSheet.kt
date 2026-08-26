package ru.andmar.flint.features.category.ui.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultModalSheetItem
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.reminder.domain.model.dateToUi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryActionsSheet(
    sheetState: SheetState,
    categoryDetails: CategoryDetails,
    onActions: (CategoryAction) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Column {
            if (categoryDetails.title.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = categoryDetails.title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
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
                            text = "${stringResource(R.string.sheet_update_title)} ${dateToUi(categoryDetails.updateTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "${stringResource(R.string.sheet_create_title)} ${dateToUi(categoryDetails.createTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    if (categoryDetails.highlight) {
                        Card(Modifier.padding(3.dp)) {
                            Text(
                                text = stringResource(R.string.sheet_highlight_title),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
                item {
                    if (categoryDetails.fix) {
                        Card(Modifier.padding(3.dp)) {
                            Text(
                                text = stringResource(R.string.sheet_fix_title),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
            Text(
                text = stringResource(R.string.actions_title),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(horizontal = 10.dp)
            )
            categoryActionsSheetItems(categoryDetails = categoryDetails) {
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