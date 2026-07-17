package com.andmar.flint

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FlintApp() {
    NavigationClient()
}

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
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold
            )
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

@Composable
fun DefaultTextField(
    value: String,
    maxLiens: Int = 1,
    label: String,
    keyboardOptions: KeyboardOptions
    = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        maxLines = maxLiens,
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = keyboardOptions,
        onValueChange = { onValueChange(it) },
        //inputTransformation = InputTransformation.maxLengthInChars(maxChar)
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
    )
}

@Composable
fun DefaultButton(
    title: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .imePadding()
    ) { Text(title) }
}

@Composable
fun DefaultMenuItem(
    icon: Int,
    iconDes: String? = null,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = iconDes,
                modifier = Modifier.padding(3.dp)
            )
            Text(
                text = title,
                modifier = Modifier.padding(3.dp)
            )
        }
    }
}

@Composable
fun DefaultLoadingDialog() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(shape = RoundedCornerShape(20.dp),) {
            CircularProgressIndicator(
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun WarningDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Warning!") },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) { Text("OK") }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) { Text("Back") }
        }
    )
}

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error!") },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) { }
        }
    )
}

@Composable
fun AuthDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Not auth!") },
        text = { Text("You need to sign in") },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) { Text("OK") }
        }
    )
}

@Composable
fun DefaultScreenText(
    text: String
) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    )
}

@Composable
fun DefaultSheetItem(
    title: Int,
    icon: Int,
    desc: String?,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = desc,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(start = 10.dp, end = 5.dp)
            )
            Text(
                text = stringResource(title),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(start = 5.dp, end = 10.dp)
            )
        }
    }
}

sealed interface FlintActions {
    object Default: FlintActions
    object Success: FlintActions
    object Loading: FlintActions
    data class Error(val message: String): FlintActions
}