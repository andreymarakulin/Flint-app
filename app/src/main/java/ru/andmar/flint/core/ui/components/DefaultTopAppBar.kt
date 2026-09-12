package ru.andmar.flint.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.andmar.flint.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    title: String = "",
    navIcon: Int? = null,
    navDes: String? = null,
    onNavIcon: () -> Unit = {},
    actionsIcon: Int? = null,
    actionsDes: String? = null,
    onActions: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            AnimatedContent(title) {
                Text(
                    text = it,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        navigationIcon = {
            if (navIcon != null) {
                IconButton(
                    onClick = onNavIcon
                ) { Icon(painterResource(navIcon), navDes) }
            }
        },
        actions = {
            if (actionsIcon != null) {
                IconButton(
                    onClick = onActions
                ) { Icon(painterResource(actionsIcon), actionsDes)}
            }
        }
    )
}
