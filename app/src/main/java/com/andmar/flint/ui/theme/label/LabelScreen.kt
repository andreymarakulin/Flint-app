package com.andmar.flint.ui.theme.label

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import kotlinx.serialization.Serializable

@Serializable
object LabelScreenRoute

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = viewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        LabelBody(
            innerPaddingValues = innerPadding,
            labelUiState = viewModel.labelUiState
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun LabelBody(
    innerPaddingValues: PaddingValues,
    labelUiState: LabelUiState,
    onActions: (LabelActions) -> Unit
) {

    when(labelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = labelUiState.flintActions.message
            ) { onActions(LabelActions.DismissError) }
        }
    }
}