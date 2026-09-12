package ru.andmar.flint.core.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.andmar.flint.R

@Composable
fun WarningDialog(
    message: String = "",
    confirmTitle: String = stringResource(R.string.ok_title),
    dismissTitle: String = stringResource(R.string.cancel_title),
    isOnlyDismiss: Boolean = true,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(painterResource(R.drawable.warning), null) },
        title = { Text(stringResource(R.string.warning_dialog_title)) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) { Text(confirmTitle) }
        },
        dismissButton = {
            if (!isOnlyDismiss) {
                TextButton(
                    onClick = onDismiss
                ) { Text(dismissTitle) }
            }
        }
    )
}