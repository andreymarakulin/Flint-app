package ru.andmar.flint.features.account.ui.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.core.ui.components.PasswordTextField
import ru.andmar.flint.features.account.domain.model.AuthDetails
import ru.andmar.flint.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {
    val signInUiState = viewModel.signInUiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        SignInBody(
            innerPaddingValues = innerPadding,
            signInUiState = signInUiState.value,
            onClickSignUp = { onNavigationRoutes(NavigationRoutes.SignUpScreenRoute) },
            onSuccess = { onNavigationRoutes(NavigationRoutes.HomeScreenRoute) }
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun SignInBody(
    innerPaddingValues: PaddingValues,
    signInUiState: SignInUiState,
    onClickSignUp: () -> Unit,
    onSuccess: () -> Unit,
    onActions: (SignInScreenActions) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_title))
        AuthDetailsForm(
            authDetails = signInUiState.authDetails
        ) { onActions(SignInScreenActions.UpdateAuthDetails(it)) }
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(vertical = 3.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.info),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.if_no_have_account),
                fontSize = 12.sp,
                modifier = Modifier.padding(3.dp)
            )
        }
        OutlinedButton(
            onClick = onClickSignUp
        ) { Text(stringResource(R.string.sign_up_title)) }
        DefaultButton(
            title = stringResource(R.string.sign_in_button),
            enabled = signInUiState.isAction
        ) { onActions(SignInScreenActions.SignInScreen) }
    }

    when(signInUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = signInUiState.flintActions.message
            ) { onActions(SignInScreenActions.DismissError) }
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
        PasswordTextField(
            value = authDetails.password,
            maxLiens = 1,
            label = stringResource(R.string.password_label),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        ) { updateAuthDetails(authDetails.copy(password = it)) }
    }
}