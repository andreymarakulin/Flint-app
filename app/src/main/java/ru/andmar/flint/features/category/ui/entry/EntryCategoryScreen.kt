package ru.andmar.flint.features.category.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.button.DefaultButton
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.text.DefaultScreenText
import ru.andmar.flint.core.ui.components.DefaultTextField
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.features.category.domain.model.CategoryDetails

@Composable
fun EntryCategoryScreen(
    viewModel: EntryCategoryViewModel = koinViewModel(),
    onNavBack: () -> Unit
) {

    val entryCategoryUiState = viewModel.entryCategoryUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                navIcon = R.drawable.arrow_back,
                navDes = "",
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EntryCategoryBody(
            innerPaddingValues = innerPadding,
            entryCategoryUiState = entryCategoryUiState.value,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EntryCategoryBody(
    innerPaddingValues: PaddingValues,
    entryCategoryUiState: EntryCategoryUiState,
    onSuccess: () -> Unit,
    onActions: (EntryCategoryScreenActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.entry_category_title))
        CategoryDetailsForm(
            categoryDetails = entryCategoryUiState.categoryDetails
        ) { onActions(EntryCategoryScreenActions.UpdateCategoryScreenDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.entry_category_button),
            enabled = entryCategoryUiState.isAction
        ) { onActions(EntryCategoryScreenActions.CreateCategoryScreen) }
    }

    when(entryCategoryUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = entryCategoryUiState.flintActions.message
            ) { onActions(EntryCategoryScreenActions.DismissError) }
        }
    }
}

@Composable
fun CategoryDetailsForm(
    categoryDetails: CategoryDetails,
    updateCategoryDetails: (CategoryDetails) -> Unit
) {
    Column {
        DefaultTextField(
            value = categoryDetails.title,
            maxLiens = 1,
            label = stringResource(R.string.title_label),
        ) { updateCategoryDetails(categoryDetails.copy(title = it)) }
    }
}