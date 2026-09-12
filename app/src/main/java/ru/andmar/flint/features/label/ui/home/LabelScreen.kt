package ru.andmar.flint.features.label.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.features.label.ui.components.LabelAction
import ru.andmar.flint.features.label.ui.components.LabelActionsSheet
import ru.andmar.flint.features.label.ui.components.cards.LabelDetailsCard
import ru.andmar.flint.navigation.NavigationRoutes

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = koinViewModel(),
    onNavigationRoutes: (NavigationRoutes) -> Unit,
    onNavBack: () -> Unit
) {

    val labelUiState = viewModel.labelUiState.collectAsStateWithLifecycle()
    val labelDetailsListState = viewModel.labelDetailsListState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val deleteLabelSnackbarTitle = stringResource(R.string.delete_note_snackbar_title)
    val cancelSnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteLabelSnackbarTitle = stringResource(R.string.move_to_basket_note_snackbar_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.labels_title),
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigationRoutes(NavigationRoutes.EntryLabelScreenRoute) }) {
                Icon(painterResource(R.drawable.add), null)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        LabelBody(
            contentPaddingValues = contentPadding,
            labelDetailsListState = labelDetailsListState.value,
            labelUiState = labelUiState.value
        ) { viewModel.onActions(it) }
    }


    LaunchedEffect(viewModel.labelUiActions) {
        viewModel.labelUiActions.collect { navigationEffect ->
            when(navigationEffect) {
                is LabelUiActions.None -> {}
                is LabelUiActions.EditLabel -> {
                    onNavigationRoutes(NavigationRoutes.EditLabelScreenRoute(navigationEffect.labelId))
                }
                is LabelUiActions.ShowDeleteSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = deleteLabelSnackbarTitle,
                        actionLabel = cancelSnackbarTitle,
                        duration = SnackbarDuration.Long
                    )
                    when(snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                           // viewModel.onActions(LabelScreenActions.LabelActions(LabelAction.RestoreNote(navigationEffect.noteDetails)))
                        }
                        SnackbarResult.Dismissed -> {} /*snackbarHostState.showSnackbar(
                            message = moveToBasketDeleteNoteSnackbarTitle,
                            duration = SnackbarDuration.Short


                        )

                        */
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelBody(
    contentPaddingValues: PaddingValues,
    labelDetailsListState: LabelDetailsListState,
    labelUiState: LabelUiState,
    onActions: (LabelScreenActions) -> Unit
) {

    val scope = rememberCoroutineScope()
    val labelActionsSheetState = rememberModalBottomSheetState()
    var showLabelActionsSheet by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = contentPaddingValues
    ) {
        items(labelDetailsListState.labelDetailsList) { labelDetails ->
            LabelDetailsCard(
                modifier = Modifier.animateItem(),
                labelDetails = labelDetails,
                onClickChoice = {
                    onActions(LabelScreenActions.LabelActions(LabelAction.ChoiceLabel(labelDetails)))
                }
            ) {
                onActions(LabelScreenActions.UpdateSelectedLabelDetails(labelDetails))
                showLabelActionsSheet = true
            }
        }
    }

    if (showLabelActionsSheet) {
        LabelActionsSheet(
            sheetState = labelActionsSheetState,
            labelDetails = labelUiState.selectedLabelDetails,
            onActions = { onActions(LabelScreenActions.LabelActions(it)) }
        ) {
            scope.launch { labelActionsSheetState.hide() }.invokeOnCompletion {
                showLabelActionsSheet = false
            }
        }
    }

    when(labelUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = labelUiState.flintActions.message
            ) { onActions(LabelScreenActions.DismissError) }
        }
    }
}