package com.andmar.flint.ui.theme.details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import com.andmar.flint.ui.theme.note.NoteActionsSheet
import com.andmar.flint.ui.theme.todo.TodoActionsSheet
import com.andmar.flint.ui.theme.todo.TodoDetailsCard
import com.andmar.flint.ui.theme.todo.TodoDetailsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class DetailsScreenRoute(val noteId: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    noteActionsSheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    onNavEditNote: (String) -> Unit,
    onNavEntryTodo: (String) -> Unit,
    onNavEditTodo: (String) -> Unit,
    onNavBack: () -> Unit
) {

    val noteDetailsState = viewModel.noteDetailsState.collectAsStateWithLifecycle()
    val todoDetailsState = viewModel.todoDetailsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Details",
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
                onClick = { onNavEntryTodo(noteDetailsState.value.noteDetails.id) },
                containerColor = MaterialTheme.colorScheme.primary
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
            detailsUiState = viewModel.detailsUiState,
            noteActionsSheetState = noteActionsSheetState,
            scope = scope,
            onClickEditNote = { onNavEditNote(it) },
            onClickEditTodo = {}
        ) { viewModel.onActions(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBody(
    todoActionsSheetState: SheetState = rememberModalBottomSheetState(),
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {

        item {
            Card(
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
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        items(todoDetailsState.todoDetailsList) { todoDetails ->
            TodoDetailsCard(
                modifier = Modifier.animateItem(),
                todoDetails = todoDetails
            ) {
                onActions(DetailsActions.UpdateSelectedTodoDetails(todoDetails))
                scope.launch { todoActionsSheetState.show() }
            }
        }
    }

    if (noteActionsSheetState.isVisible) {
        NoteActionsSheet(
            sheetState = noteActionsSheetState,
            onFix = { onActions(DetailsActions.FixNote) },
            onDone = { onActions(DetailsActions.DoneNote) },
            onHighlight = { onActions(DetailsActions.HighlightNote) },
            onEdit = { onClickEditNote(noteDetailsState.noteDetails.id) },
            onDelete = { onActions(DetailsActions.DeleteNote) }
        ) { scope.launch { noteActionsSheetState.hide() } }
    }

    if (todoActionsSheetState.isVisible) {
        TodoActionsSheet(
            sheetState = todoActionsSheetState,
            onFix = { onActions(DetailsActions.FixTodo) },
            onDone = { onActions(DetailsActions.DoneTodo) },
            onHighlight = { onActions(DetailsActions.HighlightTodo) },
            onEdit = onClickEditTodo,
            onDelete = { onActions(DetailsActions.DeleteTodo) }
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