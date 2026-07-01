package com.andmar.flint.ui.theme.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultButton
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
object SignUpScreenRoute

@Composable
fun SignUpScreen(
    viewModel: SingUpViewModel = viewModel(factory = FlintViewModelProvider.Factory),
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
        SignUpBody(
            innerPaddingValues = innerPadding,
            signUpUiState = viewModel.signUpUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun SignUpBody(
    innerPaddingValues: PaddingValues,
    signUpUiState: SignUpUiState,
    onSuccess: () -> Unit,
    onActions: (SignUpActions) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        AuthDetailsForm(
            authDetails = signUpUiState.authDetails
        ) { onActions(SignUpActions.UpdateAuthDetails(it)) }
        UserAgreementCard(
            checked = signUpUiState.isUserAgreement
        ) { onActions(SignUpActions.UpdateUserAgreement(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = signUpUiState.isAction
        ) { onActions(SignUpActions.SignUp) }
    }

    when(signUpUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = signUpUiState.flintActions.message
            ) { onActions(SignUpActions.DismissError) }
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
    }
}