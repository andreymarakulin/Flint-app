package ru.andmar.flint.features.category.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.DefaultDetails
import ru.andmar.flint.core.ui.components.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.ErrorDialog
import ru.andmar.flint.features.category.domain.model.CategoryDetails
import ru.andmar.flint.features.category.ui.components.CategoryAction
import ru.andmar.flint.features.category.ui.components.CategoryActionsSheet
import ru.andmar.flint.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val categoryDetailsListState = viewModel.categoryDetailsListState.collectAsStateWithLifecycle()
    val categoryUiState = viewModel.categoryUiState.collectAsStateWithLifecycle()

    val deleteCategorySnackbarTitle = stringResource(R.string.delete_category_snackbar_title)
    val cancelDeleteCategorySnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteCategorySnackbarTitle = stringResource(R.string.move_to_basket_category_snackbar_title)

    CategoryBody(
        categoryDetailsListState = categoryDetailsListState.value,
        categoryUiState = categoryUiState.value,
    ) { viewModel.onActions(it) }

    LaunchedEffect(viewModel.categoryUiAction) {
        viewModel.categoryUiAction.collect { navigationEffect ->
            when(navigationEffect) {
                is CategoryUiAction.None -> {}
                is CategoryUiAction.EditCategory -> {
                    onNavigationRoutes(NavigationRoutes.EditCategoryScreenRoute(navigationEffect.categoryId))
                }
                is CategoryUiAction.ShowDeleteSnackbar -> {

                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = deleteCategorySnackbarTitle,
                        actionLabel = cancelDeleteCategorySnackbarTitle,
                        duration = SnackbarDuration.Long
                    )
                    when(snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onActions(CategoryScreenActions.CategoryActions(
                                CategoryAction.RestoreCategory(navigationEffect.categoryDetails)))
                        }
                        SnackbarResult.Dismissed -> {} /*snackbarHostState.showSnackbar(
                            message = moveToBasketDeleteCategorySnackbarTitle,
                            duration = SnackbarDuration.Short
                        )
                        */
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBody(
    categoryDetailsListState: CategoryDetailsListState,
    categoryUiState: CategoryUiState,
    onActions: (CategoryScreenActions) -> Unit
) {

    val categoryActionsSheetState: SheetState = rememberModalBottomSheetState()
    val scope: CoroutineScope = rememberCoroutineScope()
    var showCategoryActionsSheet by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            key = { it.lazyKey },
            items = categoryDetailsListState.categoryDetailsList
        ) { categoryDetails ->
            CategoryDetailsCard(
                modifier = Modifier.animateItem(),
                categoryDetails = categoryDetails
            ) {
                onActions(CategoryScreenActions.UpdateSelectedCategoryDetails(categoryDetails))
                showCategoryActionsSheet = true
            }
        }
        item {
            Box(
                modifier = Modifier.height(40.dp)
            )
        }
    }

    if (showCategoryActionsSheet) {
        CategoryActionsSheet(
            sheetState = categoryActionsSheetState,
            categoryDetails = categoryUiState.selectedCategoryDetails,
            onActions = { onActions(CategoryScreenActions.CategoryActions(it)) }
        ) {
            scope.launch { categoryActionsSheetState.hide() }.invokeOnCompletion {
                showCategoryActionsSheet = false
            }
        }
    }

    when (categoryUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {}
        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = categoryUiState.flintActions.message
            ) { onActions(CategoryScreenActions.DismissError) }
        }
    }
}

@Composable
fun CategoryDetailsCard(
    modifier: Modifier,
    categoryDetails: CategoryDetails,
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
                color = if (categoryDetails.highlight) {
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
            Text(
                text = categoryDetails.title,
                fontSize = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 5.dp)
                    .weight(1f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (categoryDetails.fix) {
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