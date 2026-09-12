package ru.andmar.flint.features.todo.ui.components.cards

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import ru.andmar.flint.features.todo.domain.model.TodoDetails


@Composable
fun TodoDetailsCard(
    modifier: Modifier,
    todoDetails: TodoDetails,
    onClick: () -> Unit
) {
    var showAllTextButton by rememberSaveable { mutableStateOf(false) }
    var showAllText by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(20.dp),
                color = if (todoDetails.highlight) {
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
                if (todoDetails.title.isNotEmpty()) {
                    AnimatedContent(showAllText) {
                        Text(
                            text = todoDetails.title,
                            fontSize = 16.sp,
                            maxLines = if (it) Int.MAX_VALUE else 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = if (todoDetails.done) {
                                TextDecoration.LineThrough
                            } else TextDecoration.None,
                            onTextLayout = {
                                if (!showAllText) showAllTextButton = it.didOverflowHeight
                            },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(vertical = 5.dp)
                        )
                    }
                }
                if (todoDetails.text.isNotEmpty()) {
                    AnimatedContent(showAllText) {
                        Text(
                            text = todoDetails.text,
                            fontSize = 13.sp,
                            maxLines = if (it) Int.MAX_VALUE else 5,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = if (todoDetails.done) {
                                TextDecoration.LineThrough
                            } else TextDecoration.None,
                            onTextLayout = {
                                if (!showAllText) showAllTextButton = it.didOverflowHeight
                            },
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 5.dp)
                        )
                    }
                }
                if (todoDetails.labelDetails.id.isNotBlank()) {
                    FilterChip(
                        selected = true,
                        onClick = {},
                        leadingIcon = { Icon(painterResource(R.drawable.label), null)},
                        label = { Text(todoDetails.labelDetails.title) },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (todoDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null
                    )
                }
                if (showAllTextButton) {
                    IconToggleButton(
                        checked = showAllText,
                        onCheckedChange = { showAllText = !showAllText }
                    ) {
                        AnimatedContent(showAllText) {
                            if (it) {
                                Icon(painterResource(R.drawable.keyboard_arrow_up), null)
                            } else Icon(painterResource(R.drawable.keyboard_arrow_down), null)
                        }
                    }
                }
                Column {

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
}