package ru.andmar.flint.features.label.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.andmar.flint.R
import ru.andmar.flint.features.label.domain.model.LabelDetails
import ru.andmar.flint.features.label.ui.components.cards.ChoiceLabelDetailsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceLabelSheet(
    sheetState: SheetState,
    labelDetailsList: List<LabelDetails>,
    onClick: (LabelDetails) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(labelDetailsList) { labelDetails ->
                ChoiceLabelDetailsCard(labelDetails) {
                    onClick(labelDetails)
                    onDismiss()
                }
            }
        }
    }
}
