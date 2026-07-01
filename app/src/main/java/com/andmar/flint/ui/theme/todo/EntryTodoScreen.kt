package com.andmar.flint.ui.theme.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andmar.flint.DefaultButton
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultTextField
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
data class EntryTodoScreenRoute(val noteId: String = "")

@Composable
fun EntryTodoScreen(
    viewModel: EntryTodoViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.entry_screen_title),
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryTodoBody(
            innerPaddingValues = innerPadding,
            entryTodoUiState = viewModel.entryTodoUiState
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryTodoBody(
    innerPaddingValues: PaddingValues,
    entryTodoUiState: EntryTodoUiState,
    onActions: (EntryTodoActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.entry_todo_screen_title))
        TodoDetailsForm(
            todoDetails = entryTodoUiState.todoDetails,
        ) { onActions(EntryTodoActions.UpdateTodoDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = entryTodoUiState.isAction
        ) { onActions(EntryTodoActions.CreateTodo) }
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