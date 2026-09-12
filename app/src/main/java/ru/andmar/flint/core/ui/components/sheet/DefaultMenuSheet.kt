package ru.andmar.flint.core.ui.components.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultModalSheetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultMenuSheet(
    sheetState: SheetState,
    menuSheetItems: List<ModalSheetItem>,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss
    ) {
        menuSheetItems.forEach { item ->
            DefaultModalSheetItem(
                title = item.title,
                icon = item.icon
            ) {
                item.onClick()
                onDismiss()
            }
        }
    }
}