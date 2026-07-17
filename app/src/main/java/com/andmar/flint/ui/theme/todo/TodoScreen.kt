package com.andmar.flint.ui.theme.todo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object TodoScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavEntryTodo: (String) -> Unit,
    onNavEditTodo: (String) -> Unit,
    onNavBack: () -> Unit
) {

    val todoDetailsState = viewModel.todoDetailsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.todo_screen_title),
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavEntryTodo("") }
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        TodoBody(
            innerPaddingValues = innerPadding,
            todoDetailsState = todoDetailsState.value,
            todoUiState = viewModel.todoUiState,
            onClickEditTodo = { onNavEditTodo(viewModel.todoUiState.selectedTodoDetails.id) }
        ) { viewModel.onActions(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBody(
    todoActionsSheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    innerPaddingValues: PaddingValues,
    todoDetailsState: TodoDetailsState,
    todoUiState: TodoUiState,
    onClickEditTodo: () -> Unit,
    onActions: (TodoActions) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        items(todoDetailsState.todoDetailsList) { todoDetails ->
            TodoDetailsCard(
                modifier = Modifier.animateItem(),
                todoDetails = todoDetails
            ) {
                onActions(TodoActions.UpdateSelectedTodoDetails(todoDetails))
                scope.launch { todoActionsSheetState.show() }
            }
        }
        item {
            Text(
                text = "Нажмине на плюсик, чтобы добавить задачу",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(10.dp)
                    .padding(top = 20.dp)
            )
        }
    }

    if (todoActionsSheetState.isVisible) {
        TodoActionsSheet(
            sheetState = todoActionsSheetState,
            todoDetails = todoUiState.selectedTodoDetails,
            onFix = { onActions(TodoActions.FixTodo) },
            onDone = { onActions(TodoActions.DoneTodo) },
            onHighlight = { onActions(TodoActions.HighlightTodo) },
            onEdit = onClickEditTodo,
            onDelete = { onActions(TodoActions.DeleteTodo) }
        ) { scope.launch { todoActionsSheetState.hide() } }
    }
}

@Composable
fun TodoDetailsCard(
    modifier: Modifier,
    todoDetails: TodoDetails,
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
                color = if (todoDetails.highlight) {
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
                if (todoDetails.title.isNotEmpty()) {
                    Text(
                        text = todoDetails.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (todoDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(vertical = 5.dp)
                    )
                }
                if (todoDetails.text.isNotEmpty()) {
                    Text(
                        text = todoDetails.text,
                        fontSize = 13.sp,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (todoDetails.done) {
                            TextDecoration.LineThrough
                        } else TextDecoration.None,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .padding(bottom = 5.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (todoDetails.fix) {
                    Icon(
                        painter = painterResource(R.drawable.keep),
                        contentDescription = null,
                        //modifier = Modifier.padding(3.dp)
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