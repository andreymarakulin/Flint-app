package com.andmar.flint.ui.theme.reminder

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
data class EditReminderScreenRoute(val reminderId: String)

@Composable
fun EditReminderScreen(
    viewModel: EditReminderViewModel = viewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        EditReminderBody(
            innerPaddingValues = innerPadding,
            editReminderUiState = viewModel.editReminderUiState
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditReminderBody(
    innerPaddingValues: PaddingValues,
    editReminderUiState: EditReminderUiState,
    onActions: (EditReminderActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        ReminderDetailsForm(
            reminderDetails = editReminderUiState.reminderDetails
        ) { onActions(EditReminderActions.UpdateReminderDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = editReminderUiState.isAction
        ) { onActions(EditReminderActions.EditReminder) }
    }

    when(editReminderUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editReminderUiState.flintActions.message
            ) { onActions(EditReminderActions.DismissError) }
        }
    }
}