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
import com.andmar.flint.DefaultTextField
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
object EntryReminderScreenRoute

@Composable
fun EntryReminderScreen(
    viewModel: EntryReminderViewModel = viewModel(),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = ""
            )
        }
    ) { innerPadding ->
        EntryReminderBody(
            innerPaddingValues = innerPadding,
            entryReminderUiState = viewModel.entryReminderUiState
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryReminderBody(
    innerPaddingValues: PaddingValues,
    entryReminderUiState: EntryReminderUiState,
    onActions: (EntryReminderActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.sign_in_screen_text))
        ReminderDetailsForm(
            reminderDetails = entryReminderUiState.reminderDetails
        ) { onActions(EntryReminderActions.UpdateReminderDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryReminderUiState.isAction
        ) { onActions(EntryReminderActions.CreateReminder) }
    }

    when(entryReminderUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryReminderUiState.flintActions.message
            ) { onActions(EntryReminderActions.DismissError) }
        }
    }
}

@Composable
fun ReminderDetailsForm(
    reminderDetails: ReminderDetails,
    updateReminderDetails: (ReminderDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = reminderDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.email_label),
        ) { updateReminderDetails(reminderDetails.copy(title = it)) }
    }
}