package ru.andmar.flint.features.note.ui.components.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import ru.andmar.flint.features.note.domain.model.NoteDetails


@Composable
fun NoteDetailsCard(
    modifier: Modifier,
    noteDetails: NoteDetails,
    onClickCard: (String) -> Unit,
    onClickActions: (NoteDetails) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            //containerColor = MaterialTheme.colorScheme.primaryContainer, //сюда сделать цвет
            //contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = { onClickCard(noteDetails.id) },
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(20.dp),
                color = if (noteDetails.highlight) {
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
                if (noteDetails.title.isNotEmpty()) {
                    Text(
                        text = noteDetails.title,
                        fontSize = 16.sp,
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
                if (noteDetails.text.isNotEmpty()) {
                    Text(
                        text = noteDetails.text,
                        fontSize = 14.sp,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (noteDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 5.dp)
                    )
                }
                if (noteDetails.labelDetails.id.isNotBlank()) {
                    FilterChip(
                        selected = true,
                        onClick = {},
                        leadingIcon = { Icon(painterResource(R.drawable.label), null)},
                        label = { Text(noteDetails.labelDetails.title) },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (noteDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null,
                        //modifier = Modifier.padding(3.dp)
                    )
                }
                IconButton(
                    onClick = { onClickActions(noteDetails) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert),
                        contentDescription = null,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
    }
}