package ru.andmar.flint.features.reminder.ui.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.domain.model.dateToUiDate
import ru.andmar.flint.features.reminder.domain.model.hoursAndMinutesToUiTime

@Composable
fun EntryReminderScreen(
    viewModel: EntryReminderViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val entryReminderUiState = viewModel.entryReminderUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryReminderBody(
            innerPaddingValues = innerPadding,
            entryReminderUiState = entryReminderUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryReminderBody(
    innerPaddingValues: PaddingValues,
    entryReminderUiState: EntryReminderUiState,
    onSuccess: () -> Unit,
    onActions: (EntryReminderScreenActions) -> Unit
) {
    val datePickerDialog = rememberSaveable { mutableStateOf(false) }
    val timePickerDialog = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.entry_reminder_title))
        ReminderDetailsForm(
            reminderDetails = entryReminderUiState.reminderDetails
        ) { onActions(EntryReminderScreenActions.UpdateReminderScreenDetails(it)) }
        ReminderDateCard(
            reminderDetails = entryReminderUiState.reminderDetails,
            onClickDatePicker = {
                datePickerDialog.value = true
            },
            onClickTimePicker = {
                timePickerDialog.value = true
            }
        )
        DefaultButton(
            title = stringResource(R.string.entry_reminder_button),
            enabled = entryReminderUiState.isAction
        ) { onActions(EntryReminderScreenActions.CreateReminderScreen) }

        if (datePickerDialog.value) {
            DatePickerDialog(
                onConfirm = {
                    onActions(
                        EntryReminderScreenActions.UpdateReminderScreenDetails(
                            entryReminderUiState.reminderDetails.copy(date = it)
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
                        EntryReminderScreenActions.UpdateReminderScreenDetails(
                            entryReminderUiState.reminderDetails.copy(hours = hours, minutes = minutes)
                        )
                    )
                }
            ) {
                timePickerDialog.value = false
            }
        }
    }

    when(entryReminderUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryReminderUiState.flintActions.message
            ) { onActions(EntryReminderScreenActions.DismissError) }
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
            label = stringResource(R.string.title_label),
        ) { updateReminderDetails(reminderDetails.copy(title = it)) }
    }
}

@Composable
fun ReminderDateCard(
    reminderDetails: ReminderDetails,
    onClickDatePicker: () -> Unit,
    onClickTimePicker: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            onClick = onClickDatePicker,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(vertical = 10.dp)
                .padding(start = 10.dp, end = 5.dp)
        ) {
            Text(
                text = dateToUiDate(reminderDetails.date),
                modifier = Modifier.padding(10.dp)
            )
        }
        Card(
            onClick = onClickTimePicker,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(start = 5.dp, end = 10.dp)
        ) {
            Text(
                text = hoursAndMinutesToUiTime(
                    reminderDetails.hours,
                    reminderDetails.minutes
                ),
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    state: DatePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis()),
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(state.selectedDateMillis ?: 0)
                    onDismiss()
                }
            ) { Text(stringResource(R.string.ok_title)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel_title)) }
        }
    ) {
        DatePicker(state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: TimePickerState = rememberTimePickerState(is24Hour = true),
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    TimePickerDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.choose_time_title))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        state.hour,
                        state.minute
                    )
                    onDismiss()
                }
            ) { Text(stringResource(R.string.ok_title)) }
        },
        dismissButton = {
            TextButton(
                onClick = {

                    onDismiss()
                }
            ) { Text(stringResource(R.string.cancel_title)) }
        }
    ) {
        TimePicker(state)
    }
}