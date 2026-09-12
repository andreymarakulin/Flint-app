package ru.andmar.flint.features.account.ui.edit.password

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.PasswordTextField
import ru.andmar.flint.core.ui.components.text.DefaultScreenText

@Composable
fun UpdatePasswordScreen(
    viewModel: UpdatePasswordViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val editPasswordUiState = viewModel.updatePasswordUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { contentPadding ->
        EditPasswordBody(
            contentPaddingValues = contentPadding,
            updatePasswordUiState = editPasswordUiState.value
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditPasswordBody(
    contentPaddingValues: PaddingValues,
    updatePasswordUiState: UpdatePasswordUiState,
    onActions: (UpdatePasswordScreenActions) -> Unit
) {
    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            DefaultScreenText(stringResource(R.string.sign_in_title))
        }
        item {
            EditPasswordForm(
                password = updatePasswordUiState.password
            ) { onActions(UpdatePasswordScreenActions.UpdatePassword(it)) }
        }
        item {
            OutlinedButton(
                onClick = { onActions(UpdatePasswordScreenActions.EditPassword) },
                enabled = updatePasswordUiState.isEditPasswordAction,
                modifier = Modifier.padding(10.dp)
            ) { Text(stringResource(R.string.sign_up_title)) }
        }
    }
}

@Composable
fun EditPasswordForm(
    password: String,
    updatePassword: (String) -> Unit
) {
    PasswordTextField(
        value = password,
        maxLiens = 1,
        label = stringResource(R.string.password_label),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    ) { updatePassword(it) }
}