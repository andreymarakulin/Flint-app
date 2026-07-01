package com.andmar.flint.ui.theme.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultButton
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
data class EditLabelScreenRoute(val labelId: String)

@Composable
fun EditLabelScreen(
    viewModel: EditLabelViewModel = viewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        EditLabelBody(
            innerPaddingValues = innerPadding,
            editLabelUiState = viewModel.editLabelUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditLabelBody(
    innerPaddingValues: PaddingValues,
    editLabelUiState: EditLabelUiState,
    onSuccess: () -> Unit,
    onActions: (EditLabelActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        LabelDetailsForm(
            labelDetails = editLabelUiState.labelDetails
        ) { onActions(EditLabelActions.UpdateLabelDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = editLabelUiState.isAction
        ) { onActions(EditLabelActions.EditLabel) }
    }

    when(editLabelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editLabelUiState.flintActions.message
            ) { onActions(EditLabelActions.DismissError) }
        }
    }
}