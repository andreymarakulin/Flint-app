package com.andmar.flint.ui.theme.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultButton
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultTextField
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
object SignInScreenRoute

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavSignUp: () -> Unit,
    onNavHome: () -> Unit,
    onNavBack: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.sign_in_screen_title),
                navIcon = R.drawable.arrow_back,
                navDes = stringResource(R.string.go_back)
            )
        }
    ) { innerPadding ->
        SignInBody(
            innerPaddingValues = innerPadding,
            signInUiState = viewModel.signInUiState,
            onClickSignUp = onNavSignUp,
            onSuccess = onNavHome
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun SignInBody(
    innerPaddingValues: PaddingValues,
    signInUiState: SignInUiState,
    onClickSignUp: () -> Unit,
    onSuccess: () -> Unit,
    onActions: (SignInActions) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        AuthDetailsForm(
            authDetails = signInUiState.authDetails
        ) { onActions(SignInActions.UpdateAuthDetails(it)) }
        TextButton(
            onClick = onClickSignUp
        ) { Text(stringResource(R.string.sign_up_button)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = signInUiState.isAction
        ) { onActions(SignInActions.SignIn) }
    }

    when(signInUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = signInUiState.flintActions.message
            ) { onActions(SignInActions.DismissError) }
        }
    }
}

@Composable
fun AuthDetailsForm(
    authDetails: AuthDetails,
    updateAuthDetails: (AuthDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = authDetails.email,
            maxLiens = 1,
            label = stringResource(R.string.email_label),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        ) { updateAuthDetails(authDetails.copy(email = it)) }
        DefaultTextField(
            value = authDetails.password,
            maxLiens = 1,
            label = stringResource(R.string.password_label),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        ) { updateAuthDetails(authDetails.copy(password = it)) }
    }
}