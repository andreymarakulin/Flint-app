package ru.andmar.flint.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.andmar.flint.R

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(painterResource(R.drawable.info), null) },
        title = { Text(stringResource(R.string.error_dialog_title)) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) { Text(stringResource(R.string.repeat_title)) }
        }
    )
}