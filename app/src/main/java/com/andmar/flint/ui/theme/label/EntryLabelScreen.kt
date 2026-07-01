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
import com.andmar.flint.DefaultTextField
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
object EntryLabelScreenRoute

@Composable
fun EntryLabelScreen(
    viewModel: EntryLabelViewModel = viewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        EntryLabelBody(
            innerPaddingValues = innerPadding,
            entryLabelUiState = viewModel.entryLabelUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryLabelBody(
    innerPaddingValues: PaddingValues,
    entryLabelUiState: EntryLabelUiState,
    onSuccess: () -> Unit,
    onActions: (EntryLabelActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        LabelDetailsForm(
            labelDetails = entryLabelUiState.labelDetails
        ) { onActions(EntryLabelActions.UpdateLabelDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryLabelUiState.isAction
        ) { onActions(EntryLabelActions.CreateLabel) }
    }

    when(entryLabelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryLabelUiState.flintActions.message
            ) { onActions(EntryLabelActions.DismissError) }
        }
    }
}

@Composable
fun LabelDetailsForm(
    labelDetails: LabelDetails,
    updateLabelDetails: (LabelDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = labelDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.email_label),
        ) { updateLabelDetails(labelDetails.copy(title = it)) }
    }
}