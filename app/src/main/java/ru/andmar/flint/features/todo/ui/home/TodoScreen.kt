package ru.andmar.flint.features.todo.ui.home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.components.TodoAction
import ru.andmar.flint.features.todo.ui.components.TodoActionsSheet
import ru.andmar.flint.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val todoDetailsState = viewModel.todoDetailsState.collectAsStateWithLifecycle()
    val todoUiState = viewModel.todoUiState.collectAsStateWithLifecycle()

    val deleteTodoSnackbarTitle = stringResource(R.string.delete_todo_snackbar_title)
    val cancelTodoCategorySnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteTodoSnackbarTitle = stringResource(R.string.move_to_basket_todo_snackbar_title)

    TodoBody(
        todoDetailsState = todoDetailsState.value,
        todoUiState = todoUiState.value
    ) { viewModel.onActions(it) }

    LaunchedEffect(viewModel.todoUiAction) {
        viewModel.todoUiAction.collect { navigationEffect ->
            when(navigationEffect) {
                is TodoUiAction.None -> {}
                is TodoUiAction.EditTodo -> {
                    onNavigationRoutes(NavigationRoutes.EditTodoScreenRoute(navigationEffect.todoId))
                }
                is TodoUiAction.ShowDeleteSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = deleteTodoSnackbarTitle,
                        actionLabel = cancelTodoCategorySnackbarTitle,
                        duration = SnackbarDuration.Long
                    )
                    when(snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onActions(TodoScreenActions.TodoActions(
                                TodoAction.RestoreTodo(navigationEffect.todoDetails)))
                        }
                        SnackbarResult.Dismissed -> snackbarHostState.showSnackbar(
                            message = moveToBasketDeleteTodoSnackbarTitle,
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
fun TodoBody(
    todoDetailsState: TodoDetailsState,
    todoUiState: TodoUiState,
    onActions: (TodoScreenActions) -> Unit
) {

    val successSnackbarTitle = stringResource(R.string.success_title)

    val scope: CoroutineScope = rememberCoroutineScope()
    val todoActionsSheetState: SheetState = rememberModalBottomSheetState()
    var showTodoActionsSheet by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            key = { it.lazyKey },
           items =  todoDetailsState.todoDetailsList
        ) { todoDetails ->
            TodoDetailsCard(
                modifier = Modifier.animateItem(),
                todoDetails = todoDetails
            ) {
                onActions(TodoScreenActions.UpdateSelectedTodoDetails(todoDetails))
                showTodoActionsSheet = true
            }
        }
        item {
            Box(
                modifier = Modifier.height(40.dp)
            )
        }
    }

    if (showTodoActionsSheet) {
        TodoActionsSheet(
            sheetState = todoActionsSheetState,
            todoDetails = todoUiState.selectedTodoDetails,
            onActions = { onActions(TodoScreenActions.TodoActions(it)) }
        ) {
            scope.launch { todoActionsSheetState.hide() }.invokeOnCompletion {
                showTodoActionsSheet = false
            }
        }
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