package ru.andmar.flint.features.reminder.ui.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.core.ui.components.WarningDialog
import ru.andmar.flint.features.reminder.domain.model.ReminderDetails
import ru.andmar.flint.features.reminder.domain.model.dateToUi
import ru.andmar.flint.features.reminder.ui.components.ReminderAction
import ru.andmar.flint.features.reminder.ui.components.ReminderActionSheet
import ru.andmar.flint.features.todo.ui.components.TodoAction
import ru.andmar.flint.features.todo.ui.home.TodoScreenActions
import ru.andmar.flint.features.todo.ui.home.TodoUiAction
import ru.andmar.flint.navigation.NavigationRoutes

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val reminderDetailsListState = viewModel.reminderDetailsListState.collectAsStateWithLifecycle()
    val reminderUiState = viewModel.reminderUiState.collectAsStateWithLifecycle()

    val deleteReminderSnackbarTitle = stringResource(R.string.delete_reminder_snackbar_title)
    val cancelReminderCategorySnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteReminderSnackbarTitle = stringResource(R.string.move_to_basket_reminder_snackbar_title)

    ReminderBody(
        snackbarHostState = snackbarHostState,
        reminderUiState = reminderUiState.value,
        reminderDetailsListState = reminderDetailsListState.value
    ) { viewModel.onActions(it) }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

        when {
            permissionState.status.shouldShowRationale -> {
                WarningDialog(
                    message = stringResource(R.string.permissions_notification_title),
                    isOnlyDismiss = true
                ) { permissionState.launchPermissionRequest() }
            }
            permissionState.status.isGranted -> {

            }
            else -> {
                WarningDialog(
                    message = stringResource(R.string.permissions_notification_title),
                    isOnlyDismiss = true
                ) { permissionState.launchPermissionRequest() }
            }
        }
    }
    LaunchedEffect(viewModel.reminderUiAction) {
        viewModel.reminderUiAction.collect { navigationEffect ->
            when(navigationEffect) {
                is ReminderUiAction.None -> {}
                is ReminderUiAction.EditReminder -> {
                    onNavigationRoutes(NavigationRoutes.EditNoteScreenRoute(navigationEffect.reminderId))
                }
                is ReminderUiAction.ShowDeleteSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = deleteReminderSnackbarTitle,
                        actionLabel = cancelReminderCategorySnackbarTitle,
                        duration = SnackbarDuration.Long
                    )
                    when(snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onActions(ReminderScreenActions.ReminderActions(
                                    ReminderAction.RestoreReminder(navigationEffect.reminderDetails)))
                        }
                        SnackbarResult.Dismissed -> snackbarHostState.showSnackbar(
                            message = moveToBasketDeleteReminderSnackbarTitle,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderBody(
    snackbarHostState: SnackbarHostState,
    reminderUiState: ReminderUiState,
    reminderDetailsListState: ReminderDetailsListState,
    onActions: (ReminderScreenActions) -> Unit
) {

    val successSnackbarTitle = stringResource(R.string.success_title)

    val scope = rememberCoroutineScope()
    val reminderActionsSheetState: SheetState = rememberModalBottomSheetState()
    var showReminderActionsSheet by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            key = { it.lazyKey },
            items = reminderDetailsListState.reminderDetailsList
        ) { reminderDetails ->
            ReminderDetailsCard(
                modifier = Modifier.animateItem(),
                reminderDetails = reminderDetails
            ) {
                onActions(ReminderScreenActions.UpdateSelectedReminderDetails(reminderDetails))
                showReminderActionsSheet = true
            }
        }
        item {
            Box(
                modifier = Modifier.height(60.dp)
            )
        }
    }

    if (showReminderActionsSheet) {
        ReminderActionSheet(
            sheetState = reminderActionsSheetState,
            reminderDetails = reminderUiState.selelctedReminderDetails,
            onActions = { onActions(ReminderScreenActions.ReminderActions(it)) }
        ) {
            scope.launch { reminderActionsSheetState.hide() }.invokeOnCompletion {
                showReminderActionsSheet = false
            }
        }
    }

    when(reminderUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = successSnackbarTitle,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = reminderUiState.flintActions.message
            ) { onActions(ReminderScreenActions.DismissError) }
        }
    }
}

@Composable
fun ReminderDetailsCard(
    modifier: Modifier,
    reminderDetails: ReminderDetails,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(vertical = 5.dp)
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(20.dp),
                color = if (reminderDetails.highlight) {
                    MaterialTheme.colorScheme.primary
                } else Color.Transparent
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(Modifier.weight(1f)) {
                if (reminderDetails.title.isNotEmpty()) {
                    Text(
                        text = reminderDetails.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (reminderDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
                Text(
                    text = dateToUi(reminderDetails.reminderDate),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 5.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (reminderDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.more_vert),
                        contentDescription = null,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }
        }
    }
}

