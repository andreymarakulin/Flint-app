package ru.andmar.flint.features.label.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.button.DefaultButton
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.core.ui.components.text.DefaultScreenText
import ru.andmar.flint.features.label.domain.model.LabelDetails

@Composable
fun EntryLabelScreen(
    viewModel: EntryLabelViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val entryLabelUiState = viewModel.entryLabelUiState.collectAsStateWithLifecycle()

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
            entryLabelUiState = entryLabelUiState.value,
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
        DefaultScreenText(stringResource(R.string.entry_label_title))
        LabelDetailsForm(
            labelDetails = entryLabelUiState.labelDetails
        ) { onActions(EntryLabelScreenActions.UpdateLabelScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.entry_label_button),
            enabled = entryLabelUiState.isAction
        ) { onActions(EntryLabelScreenActions.CreateLabel) }
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
            label = stringResource(R.string.title_label),
        ) { updateLabelDetails(labelDetails.copy(title = it)) }
        DefaultTextField(
            value = labelDetails.text,
            maxLiens = 1,
            label = stringResource(R.string.text_label),
        ) { updateLabelDetails(labelDetails.copy(text = it)) }
    }
}