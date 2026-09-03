package ru.andmar.flint.features.todo.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultButton
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.todo.domain.model.TodoDetails
import ru.andmar.flint.features.todo.ui.components.TodoAction

@Composable
fun EntryTodoScreen(
    viewModel: EntryTodoViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val entryTodoUiState = viewModel.entryTodoUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryTodoBody(
            innerPaddingValues = innerPadding,
            entryTodoUiState = entryTodoUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryTodoBody(
    innerPaddingValues: PaddingValues,
    entryTodoUiState: EntryTodoUiState,
    onSuccess: () -> Unit,
    onActions: (EntryTodoScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.entry_todo_title))
        TodoDetailsForm(
            todoDetails = entryTodoUiState.todoDetails,
        ) { onActions(EntryTodoScreenActions.UpdateTodoScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.entry_todo_button),
            enabled = entryTodoUiState.isAction
        ) { onActions(EntryTodoScreenActions.CreateTodoScreen) }
    }

    when(entryTodoUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryTodoUiState.flintActions.message
            ) { onActions(EntryTodoScreenActions.DismissError) }
        }
    }
}

@Composable
fun TodoDetailsForm(
    todoDetails: TodoDetails,
    updateTodoDetails: (TodoDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = todoDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.title_label),
        ) { updateTodoDetails(todoDetails.copy(title = it)) }
        DefaultTextField(
            value = todoDetails.text,
            maxLiens = 6,
            label = stringResource(R.string.text_label),
        ) { updateTodoDetails(todoDetails.copy(text = it)) }
    }
}