package ru.andmar.flint.features.label.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.label.domain.model.LabelDetails

@Serializable
object EntryLabelScreenRoute

@Composable
fun EntryLabelScreen(
    viewModel: EntryLabelViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "",
                navIcon = R.drawable.arrow_back,
                navDes = "back",
                onNavIcon = onNavBack
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
    onActions: (EntryLabelScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
       // DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        LabelDetailsForm(
            labelDetails = entryLabelUiState.labelDetails
        ) { onActions(EntryLabelScreenActions.UpdateLabelScreenDetails(it)) }
        /*
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryLabelUiState.isAction
        ) { onActions(EntryLabelScreenActions.CreateLabel) }

         */
    }

    when(entryLabelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryLabelUiState.flintActions.message
            ) { onActions(EntryLabelScreenActions.DismissError) }
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