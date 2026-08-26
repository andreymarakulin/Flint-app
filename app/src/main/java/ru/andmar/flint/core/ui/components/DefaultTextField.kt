package ru.andmar.flint.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.andmar.flint.R


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
fun PasswordTextField(
    value: String,
    maxLiens: Int = 1,
    label: String,
    keyboardOptions: KeyboardOptions
    = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
) {

    var visibility by rememberSaveable { mutableStateOf(false) }



    OutlinedTextField(
        value = value,
        maxLines = maxLiens,
        shape = RoundedCornerShape(20.dp),
        keyboardOptions = keyboardOptions,
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = { onValueChange(it) },
        //inputTransformation = InputTransformation.maxLengthInChars(maxChar)
        label = { Text(label) },
        trailingIcon = {
            IconButton(
                onClick = { visibility = !visibility }
            ) {
                Icon(
                    painter = painterResource(if (visibility) R.drawable.visibility else R.drawable.visibility_off),
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
    )
}