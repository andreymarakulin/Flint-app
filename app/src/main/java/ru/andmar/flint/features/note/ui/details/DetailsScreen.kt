package ru.andmar.flint.features.note.ui.details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.components.TodoActionsSheet
import ru.andmar.flint.features.todo.ui.home.TodoDetailsCard
import ru.andmar.flint.features.todo.ui.home.TodoDetailsState
import ru.andmar.flint.navigation.NavigationRoutes

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
    val scope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Заметка",
                navIcon = R.drawable.arrow_back,
                navDes = "back",
                onNavIcon = onNavBack,
                actionsIcon = R.drawable.more_vert,
                actionsDes = "more",
                onActions = { scope.launch { noteActionsSheetState.show() } }
            )
        },
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
            noteDetailsState = noteDetailsState.value,
            todoDetailsState = todoDetailsState.value,
            detailsUiState = detailsUiState.value,
            noteActionsSheetState = noteActionsSheetState,
            scope = scope,
            onClickEditNote = { onNavigationRoutes(NavigationRoutes.EditNoteScreenRoute(it)) },
            onClickEditTodo = {
                /*
                onNavigationRoutes(
                    NavigationRoutes.EditTodoScreenRoute()
                )

                 */
            },
        ) { viewModel.onActions(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBody(
    innerPaddingValues: PaddingValues,
    noteDetailsState: NoteDetailsState,
    todoDetailsState: TodoDetailsState,
    detailsUiState: DetailsUiState,
    noteActionsSheetState: SheetState,
    scope: CoroutineScope,
    onClickEditNote: (String) -> Unit,
    onClickEditTodo: () -> Unit,
    onActions: (DetailsActions) -> Unit
) {
   // val isEditNoteTitle = rememberSaveable { mutableStateOf(false) }
   // val isEditNoteText = rememberSaveable { mutableStateOf(false) }

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
                if (noteDetailsState.noteDetails.fix) {
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

    if (noteActionsSheetState.isVisible) {
        /*
        NoteActionsSheet(
            sheetState = noteActionsSheetState,
            noteDetails = noteDetailsState.noteDetails,

        ) { scope.launch { noteActionsSheetState.hide() } }

         */
    }

    if (showTodoActionsSheet) {
        TodoActionsSheet(
            sheetState = todoActionsSheetState,
            todoDetails = selectedTodoDetails,
            onActions = { }
        ) { scope.launch { todoActionsSheetState.hide() } }
    }

    when(detailsUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = detailsUiState.flintActions.message
            ) { onActions(DetailsActions.DismissError) }
        }
    }
}

@Composable
fun NoteDetailsCard() {

}