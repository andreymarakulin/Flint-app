package ru.andmar.flint.features.todo.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.features.label.ui.components.ChoiceLabelSheet
import ru.andmar.flint.features.todo.ui.components.TodoAction
import ru.andmar.flint.features.todo.ui.components.TodoActionsSheet
import ru.andmar.flint.features.todo.ui.components.cards.TodoDetailsCard
import ru.andmar.flint.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val labelDetailsListState = viewModel.labelDetailsListState.collectAsStateWithLifecycle()
    val todoDetailsState = viewModel.todoDetailsState.collectAsStateWithLifecycle()
    val todoUiState = viewModel.todoUiState.collectAsStateWithLifecycle()

    val deleteTodoSnackbarTitle = stringResource(R.string.delete_todo_snackbar_title)
    val cancelTodoCategorySnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteTodoSnackbarTitle = stringResource(R.string.move_to_basket_todo_snackbar_title)

    val scope = rememberCoroutineScope()
    val choiceLabelSheetState = rememberModalBottomSheetState()
    var showChoiceLabelSheet by rememberSaveable { mutableStateOf(false) }

    TodoBody(
        todoDetailsState = todoDetailsState.value,
        todoUiState = todoUiState.value
    ) { viewModel.onActions(it) }

    LaunchedEffect(viewModel.todoUiAction) {
        viewModel.todoUiAction.collect { navigationEffect ->
            when(navigationEffect) {
                is TodoUiAction.None -> {}
                is TodoUiAction.ChoiceLabelSheet -> {
                    showChoiceLabelSheet = true
                }
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

    if (showChoiceLabelSheet) {
        ChoiceLabelSheet(
            sheetState = choiceLabelSheetState,
            labelDetailsList = labelDetailsListState.value.labelDetailsList,
            onClick = { viewModel.onActions(TodoScreenActions.EditLabel(it))}
        ) {
            scope.launch { choiceLabelSheetState.hide() }.invokeOnCompletion {
                showChoiceLabelSheet = false
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
            if (todoDetailsState.todoDetailsDoneList.isNotEmpty()) {
                Text(
                    text = "Выполненные",
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
        items(
            key = { it.lazyKey },
            items =  todoDetailsState.todoDetailsDoneList
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