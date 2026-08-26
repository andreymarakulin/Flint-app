package ru.andmar.flint.features.reminder.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.reminder.ui.entry.DatePickerDialog
import ru.andmar.flint.features.reminder.ui.entry.ReminderDateCard
import ru.andmar.flint.features.reminder.ui.entry.ReminderDetailsForm
import ru.andmar.flint.features.reminder.ui.entry.TimePickerDialog

@Serializable
data class EditReminderScreenRoute(val reminderId: String)

@Composable
fun EditReminderScreen(
    viewModel: EditReminderViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val editReminderUiState = viewModel.editReminderUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EditReminderBody(
            innerPaddingValues = innerPadding,
            editReminderUiState = editReminderUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReminderBody(
    innerPaddingValues: PaddingValues,
    editReminderUiState: EditReminderUiState,
    onSuccess: () -> Unit,
    onActions: (EditReminderScreenActions) -> Unit
) {
    val datePickerDialog = rememberSaveable { mutableStateOf(false) }
    val timePickerDialog = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_reminder_title))
        ReminderDetailsForm(
            reminderDetails = editReminderUiState.reminderDetails
        ) { onActions(EditReminderScreenActions.UpdateReminderScreenDetails(it)) }
        ReminderDateCard(
            reminderDetails = editReminderUiState.reminderDetails,
            onClickDatePicker = {
                datePickerDialog.value = true
            },
            onClickTimePicker = {
                timePickerDialog.value = true
            }
        )
        DefaultButton(
            title = stringResource(R.string.edit_reminder_button),
            enabled = editReminderUiState.isAction
        ) { onActions(EditReminderScreenActions.EditReminderScreen) }
    }

    if (datePickerDialog.value) {
        DatePickerDialog(
            onConfirm = {
                onActions(
                    EditReminderScreenActions.UpdateReminderScreenDetails(
                        editReminderUiState.reminderDetails.copy(date = it)
                    )
                )
            }
        ) {
            datePickerDialog.value = false
        }
    }

    if (timePickerDialog.value) {
        TimePickerDialog(
            onConfirm = { hours, minutes ->
                onActions(
                    EditReminderScreenActions.UpdateReminderScreenDetails(
                        editReminderUiState.reminderDetails.copy(hours = hours, minutes = minutes)
                    )
                )
            }
        ) {
            timePickerDialog.value = false
        }
    }

    when(editReminderUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editReminderUiState.flintActions.message
            ) { onActions(EditReminderScreenActions.DismissError) }
        }
    }
}