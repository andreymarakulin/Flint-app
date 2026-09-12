package ru.andmar.flint.features.note.ui.details

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.note.ui.components.NoteActionsSheet
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.components.TodoActionsSheet
import ru.andmar.flint.features.todo.ui.components.cards.TodoDetailsCard
import ru.andmar.flint.features.todo.ui.home.TodoDetailsState
import ru.andmar.flint.navigation.NavigationRoutes
import ru.andmar.flint.ui.main.MainScreenActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = koinViewModel(),
    onNavigationRoutes: (NavigationRoutes) -> Unit,
    onNavBack: () -> Unit
) {

    val noteDetailsState = viewModel.noteDetailsState.collectAsStateWithLifecycle()
    val todoDetailsState = viewModel.todoDetailsState.collectAsStateWithLifecycle()
    val detailsUiState = viewModel.detailsUiState.collectAsStateWithLifecycle()

    val noteActionsSheetState: SheetState = rememberModalBottomSheetState()
    val showNoteActionsSheet = rememberSaveable { mutableStateOf(false) }
    val scope: CoroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val deleteNoteSnackbarTitle = stringResource(R.string.delete_note_snackbar_title)
    val cancelNoteCategorySnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteNoteSnackbarTitle = stringResource(R.string.move_to_basket_note_snackbar_title)

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Заметка",
                navIcon = R.drawable.arrow_back,
                navDes = "back",
                onNavIcon = onNavBack,
                actionsIcon = R.drawable.more_vert,
                actionsDes = "more",
                onActions = { showNoteActionsSheet.value = true }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigationRoutes(NavigationRoutes.EntryTodoScreenRoute(
                        noteDetailsState.value.noteDetails.id)
                    )
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        DetailsBody(
            innerPaddingValues = innerPadding,
            snackbarHostState = snackbarHostState,
            noteDetailsState = noteDetailsState.value,
            todoDetailsState = todoDetailsState.value,
            detailsUiState = detailsUiState.value,
            noteActionsSheetState = noteActionsSheetState,
            showNoteActionsSheet = showNoteActionsSheet,
            scope = scope,
            onClickEditNote = { onNavigationRoutes(NavigationRoutes.EditNoteScreenRoute(it))
            Log.i("noteId", it)
            }
        ) { viewModel.onActions(it) }
    }

    LaunchedEffect(viewModel.detailsUiAction) {
        viewModel.detailsUiAction.collect { navigationEffect ->
            when(navigationEffect) {
                is DetailsUiAction.None -> {}
                is DetailsUiAction.EditNote -> {
                    onNavigationRoutes(NavigationRoutes.EditNoteScreenRoute(navigationEffect.noteId))
                }
                is DetailsUiAction.EditTodo -> {
                    onNavigationRoutes(NavigationRoutes.EditTodoScreenRoute(navigationEffect.todoId))
                }
                is DetailsUiAction.ShowDeleteSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = deleteNoteSnackbarTitle,
                        actionLabel = cancelNoteCategorySnackbarTitle,
                        duration = SnackbarDuration.Long
                    )
                    when(snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onActions(DetailsScreenActions.NoteActions(NoteAction.RestoreNote(navigationEffect.noteDetails)))
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
fun DetailsBody(
    innerPaddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    noteDetailsState: NoteDetailsState,
    todoDetailsState: TodoDetailsState,
    detailsUiState: DetailsUiState,
    noteActionsSheetState: SheetState,
    showNoteActionsSheet: MutableState<Boolean>,
    scope: CoroutineScope,
    onClickEditNote: (String) -> Unit,
    onActions: (DetailsScreenActions) -> Unit
) {
    //val isEditNoteTitle = rememberSaveable { mutableStateOf(false) }
    //val isEditNoteText = rememberSaveable { mutableStateOf(false) }

    val successSnackbarTitle = stringResource(R.string.success_title)

    val todoActionsSheetState: SheetState = rememberModalBottomSheetState()
    var showTodoActionsSheet by rememberSaveable { mutableStateOf(false) }
    var selectedTodoDetails by remember { mutableStateOf(TodoDetails()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        item {
            //NoteDetailsCard() потом доделать
            Card(
                onClick = { onClickEditNote(noteDetailsState.noteDetails.id) },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(
                        width = 3.dp,
                        shape = RoundedCornerShape(20.dp),
                        color = if (noteDetailsState.noteDetails.highlight) {
                            MaterialTheme.colorScheme.primary
                        } else Color.Transparent
                    )
            ) {
                if (noteDetailsState.noteDetails.title.isNotEmpty()) {
                    /*
                    AnimatedContent(isEditNoteTitle.value) { state ->
                        if (state) {
                            DefaultTextField(
                                value = noteDetailsState.noteDetails.title,
                                maxLiens = 1,
                                label = stringResource(R.string.title_label),
                            ) { onActions(DetailsActions.)updateNoteDetails(noteDetails.copy(title = it)) }
                        }
                    }

                     */
                    Text(
                        text = noteDetailsState.noteDetails.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (noteDetailsState.noteDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
                if (noteDetailsState.noteDetails.text.isNotEmpty()) {
                    Text(
                        text = noteDetailsState.noteDetails.text,
                        fontSize = 13.sp,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (noteDetailsState.noteDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 5.dp)
                    )
                }
                /*
                if (noteDetailsState.noteDetails.fix) {

                    InputChip(
                        onClick = {},
                        label = { Text("Fix") },
                        selected = true,
                        avatar = {
                            Icon(
                                painter = painterResource(R.drawable.keep),
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.close),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(10.dp)
                    )


                 */
                    /*
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.keep),
                            contentDescription = null
                        )
                        Text(
                            text = "Fix",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 5.dp)
                        )
                    }


                     */

              //  }

                if (noteDetailsState.noteDetails.labelDetails.id.isNotBlank()) {
                    FilterChip(
                        selected = true,
                        onClick = { onActions(DetailsScreenActions.DeleteNoteLabel(noteDetailsState.noteDetails)) },
                        label = { Text(noteDetailsState.noteDetails.labelDetails.title) },
                        trailingIcon = { Icon(painterResource(R.drawable.close), null) },
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }
        }

        item {
            if (todoDetailsState.todoDetailsList.isNotEmpty()) {
                Text(
                    text = "Задачи",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp, bottom = 10.dp)
                )
            }
        }

        items(todoDetailsState.todoDetailsList) { todoDetails ->
            TodoDetailsCard(
                modifier = Modifier.animateItem(),
                todoDetails = todoDetails
            ) {
                selectedTodoDetails = todoDetails
                showTodoActionsSheet = true
            }
        }
        item {
            Box(
                modifier = Modifier.height(40.dp)
            )
        }
    }

    if (showNoteActionsSheet.value) {
        NoteActionsSheet(
            sheetState = noteActionsSheetState,
            noteDetails = noteDetailsState.noteDetails,
            onActions = { onActions(DetailsScreenActions.NoteActions(it)) }
        ) {
            scope.launch { noteActionsSheetState.hide() }.invokeOnCompletion {
                showNoteActionsSheet.value = false
            }
        }
    }

    if (showTodoActionsSheet) {
        TodoActionsSheet(
            sheetState = todoActionsSheetState,
            todoDetails = selectedTodoDetails,
            onActions = { onActions(DetailsScreenActions.TodoActions(it)) }
        ) { scope.launch { todoActionsSheetState.hide() } }
    }

    when(detailsUiState.flintActions) {
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
                message = detailsUiState.flintActions.message
            ) { onActions(DetailsScreenActions.DismissError) }
        }
    }
}

@Composable
fun NoteDetailsCard() {

}