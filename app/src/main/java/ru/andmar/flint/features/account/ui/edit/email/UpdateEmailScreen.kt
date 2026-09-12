package ru.andmar.flint.features.account.ui.edit.email

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
import ru.andmar.flint.core.ui.components.text.DefaultScreenText

@Composable
fun UpdateEmailScreen(
    viewModel: UpdateEmailViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val changeEmailUiState = viewModel.updateEmailUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { contentPadding ->
        ChangeEmailBody(
            contentPaddingValues = contentPadding,
            updateEmailUiState = changeEmailUiState.value
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun ChangeEmailBody(
    contentPaddingValues: PaddingValues,
    updateEmailUiState: UpdateEmailUiState,
    onActions: (UpdateEmailScreenActions) -> Unit
) {
    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            DefaultScreenText(stringResource(R.string.sign_in_title))
        }
        item {
            UpdateEmailForm(
                email = updateEmailUiState.email
            ) { onActions(UpdateEmailScreenActions.UpdateEmail(it)) }
        }
        item {
            OutlinedButton(
                onClick = { onActions(UpdateEmailScreenActions.ChangeEmail) },
                enabled = updateEmailUiState.isChangeEmailAction,
                modifier = Modifier.padding(10.dp)
            ) { Text(stringResource(R.string.sign_up_title)) }
        }
    }
}

@Composable
fun UpdateEmailForm(
    email: String,
    updateEmail: (String) -> Unit
) {
    DefaultTextField(
        value = email,
        maxLiens = 1,
        label = stringResource(R.string.email_label),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    ) { updateEmail(it) }
}