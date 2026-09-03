package ru.andmar.flint.features.todo.ui.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.todo.ui.entry.TodoDetailsForm

@Composable
fun EditTodoScreen(
    viewModel: EditTodoViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val editTodoUiState = viewModel.editTodoUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EditTodoBody(
            innerPaddingValues = innerPadding,
            editTodoUiState = editTodoUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditTodoBody(
    innerPaddingValues: PaddingValues,
    editTodoUiState: EditTodoUiState,
    onSuccess: () -> Unit,
    onActions: (EditTodoScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_todo_title))
        TodoDetailsForm(
            todoDetails = editTodoUiState.todoDetails,
        ) { onActions(EditTodoScreenActions.UpdateTodoScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.edit_todo_button),
            enabled = editTodoUiState.isAction
        ) { onActions(EditTodoScreenActions.EditTodoScreen) }
    }

    when(editTodoUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editTodoUiState.flintActions.message
            ) { onActions(EditTodoScreenActions.DismissError) }
        }
    }
}