package ru.andmar.flint.core.ui.components.sheet

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
import ru.andmar.flint.core.ui.components.DefaultDetails
import ru.andmar.flint.core.ui.components.DefaultModalSheetItem
import ru.andmar.flint.features.reminder.domain.model.dateToUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: DefaultDetails> ActionsSheet(
    sheetState: SheetState,
    details: T,
    actionsSheetItems: List<ModalSheetItem>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Column {
            if (details.title.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                ) {
                    Text(
                        text = details.title,
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
                            text = "${stringResource(R.string.sheet_update_title)} ${dateToUi(details.updateTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    Card(Modifier.padding(3.dp)) {
                        Text(
                            text = "${stringResource(R.string.sheet_create_title)} ${dateToUi(details.createTime)}",
                            fontSize = 12.sp,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                item {
                    if (details.highlight) {
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
                    if (details.fix) {
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
            actionsSheetItems.forEach { item ->
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