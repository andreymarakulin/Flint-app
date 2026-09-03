package ru.andmar.flint.features.account.ui.signUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.account.ui.signIn.AuthDetailsForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {
    val signUpUiState = viewModel.signUpUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        SignUpBody(
            innerPaddingValues = innerPadding,
            signUpUiState = signUpUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun SignUpBody(
    innerPaddingValues: PaddingValues,
    signUpUiState: SignUpUiState,
    onSuccess: () -> Unit,
    onActions: (SignUpScreenActions) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_up_title))
        AuthDetailsForm(
            authDetails = signUpUiState.authDetails
        ) { onActions(SignUpScreenActions.UpdateAuthDetails(it)) }
        UserAgreementCard(
            checked = signUpUiState.isUserAgreement
        ) { /*onActions(SignUpScreenActions.UpdateUserAgreement(it))*/ }
        DefaultButton(
            title = stringResource(R.string.sign_up_button),
            enabled = signUpUiState.isAction
        ) { onActions(SignUpScreenActions.SignUpScreen) }
    }

    when(signUpUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = signUpUiState.flintActions.message
            ) { onActions(SignUpScreenActions.DismissError) }
        }
    }
}

@Composable
fun UserAgreementCard(
    checked: Boolean,
    updateIsUserAgreement: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
    ) {
        /*
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.user_agreement_title),
                fontSize = 13.sp,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(5.dp)
            )
            Switch(
                checked = checked,
                onCheckedChange = { updateIsUserAgreement(it) },
                modifier = Modifier.padding(5.dp)
            )
        }

         */
    }
}