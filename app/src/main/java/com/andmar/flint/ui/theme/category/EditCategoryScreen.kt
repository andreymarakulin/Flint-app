package com.andmar.flint.ui.theme.category

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
import com.andmar.flint.DefaultLoadingDialog
import com.andmar.flint.DefaultScreenText
import com.andmar.flint.DefaultTopAppBar
import com.andmar.flint.ErrorDialog
import com.andmar.flint.FlintActions
import com.andmar.flint.FlintViewModelProvider
import com.andmar.flint.R
import kotlinx.serialization.Serializable

@Serializable
data class EditCategoryScreenRoute(val categoryId: String)

@Composable
fun EditCategoryScreen(
    viewModel: EditCategoryViewModel = viewModel(factory = FlintViewModelProvider.Factory),
    onNavBack: () -> Unit
) {

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "",
                navIcon = R.drawable.arrow_back,
                navDes = null,
                onNavIcon = onNavBack
            )
        }
    ) { innerPadding ->
        EditCategoryBody(
            innerPaddingValues = innerPadding,
            editCategoryUiState = viewModel.editCategoryUiState,
            onSuccess = onNavBack
        ) { viewModel.onActions(it) }
    }
}

@Composable
fun EditCategoryBody(
    innerPaddingValues: PaddingValues,
    editCategoryUiState: EditCategoryUiState,
    onSuccess: () -> Unit,
    onActions: (EditCategoryActions) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        DefaultScreenText(stringResource(R.string.edit_category_screen_title))
        CategoryDetailsForm(
            categoryDetails = editCategoryUiState.categoryDetails
        ) { onActions(EditCategoryActions.UpdateCategoryDetails(it)) }
        DefaultButton(
            title = stringResource(R.string.continue_button),
            enabled = editCategoryUiState.isAction
        ) { onActions(EditCategoryActions.EditCategory) }
    }

    when(editCategoryUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> onSuccess()
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = editCategoryUiState.flintActions.message
            ) { onActions(EditCategoryActions.DismissError) }
        }
    }
}