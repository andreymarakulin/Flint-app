package ru.andmar.flint.features.label.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.button.DefaultButton
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.core.ui.components.text.DefaultScreenText
import ru.andmar.flint.features.label.ui.entry.LabelDetailsForm

@Composable
fun EditLabelScreen(
    viewModel: EditLabelViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val editLabelUiState = viewModel.editLabelUiState.collectAsStateWithLifecycle()

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
        EditLabelBody(
            innerPaddingValues = innerPadding,
            editLabelUiState = editLabelUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditLabelBody(
    innerPaddingValues: PaddingValues,
    editLabelUiState: EditLabelUiState,
    onSuccess: () -> Unit,
    onActions: (EditLabelScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_label_title))
        LabelDetailsForm(
            labelDetails = editLabelUiState.labelDetails
        ) { onActions(EditLabelScreenActions.UpdateLabelScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.edit_label_button),
            enabled = editLabelUiState.isAction
        ) { onActions(EditLabelScreenActions.EditLabel) }
    }

    when(editLabelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editLabelUiState.flintActions.message
            ) { onActions(EditLabelScreenActions.DismissError) }
        }
    }
}