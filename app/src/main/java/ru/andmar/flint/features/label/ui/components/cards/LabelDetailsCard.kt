package ru.andmar.flint.features.label.ui.components.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

@Composable
fun LabelDetailsCard(
    modifier: Modifier,
    labelDetails: LabelDetails,
    onClickChoice: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(20.dp),
                color = if (labelDetails.highlight) {
                    MaterialTheme.colorScheme.primary
                } else Color.Transparent
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(Modifier.weight(1f)) {
                if (labelDetails.title.isNotEmpty()) {
                    Text(
                        text = labelDetails.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
                if (labelDetails.text.isNotEmpty()) {
                    Text(
                        text = labelDetails.text,
                        fontSize = 13.sp,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (labelDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (labelDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null
                    )
                }
                Checkbox(
                    checked = labelDetails.choice,
                    onCheckedChange = onClickChoice
                )
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
