package com.andmar.flint.ui.theme.reminder

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
object ReminderScreenRoute

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = viewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        ReminderBody(
            innerPaddingValues = innerPadding,
            reminderUiState = viewModel.reminderUiState
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun ReminderBody(
    innerPaddingValues: PaddingValues,
    reminderUiState: ReminderUiState,
    onActions: (ReminderActions) -> Unit
) {

    when(reminderUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}//onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = reminderUiState.flintActions.message
            ) { onActions(ReminderActions.DismissError) }
        }
    }
}