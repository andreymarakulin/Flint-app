package ru.andmar.flint.features.note.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.core.ui.components.dialog.DefaultLoadingDialog
import ru.andmar.flint.core.ui.components.dialog.ErrorDialog
import ru.andmar.flint.features.category.ui.components.CategoryActionsSheet
import ru.andmar.flint.features.label.ui.components.ChoiceLabelSheet
import ru.andmar.flint.features.note.domain.model.NoteDetails
import ru.andmar.flint.features.note.ui.components.NoteAction
import ru.andmar.flint.features.note.ui.components.NoteActionsSheet
import ru.andmar.flint.features.note.ui.components.cards.NoteDetailsCard
import ru.andmar.flint.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onChangeCategoryId: (String) -> Unit,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val noteUiState = viewModel.noteUiState.collectAsStateWithLifecycle()
    val noteContentState = viewModel.noteContentState.collectAsStateWithLifecycle()
    val labelDetailsListState = viewModel.labelDetailsListState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val choiceLabelSheetState = rememberModalBottomSheetState()
    var showChoiceLabelSheetState by rememberSaveable { mutableStateOf(false) }

    val deleteNoteSnackbarTitle = stringResource(R.string.delete_note_snackbar_title)
    val cancelNoteCategorySnackbarTitle = stringResource(R.string.cancel_title)
    val moveToBasketDeleteNoteSnackbarTitle = stringResource(R.string.move_to_basket_note_snackbar_title)

    val pagerState: PagerState = rememberPagerState(
        pageCount = { noteContentState.value.filteredCategoryDetailsList.size }
    )

    LaunchedEffect(pagerState.currentPage, noteContentState.value.filteredCategoryDetailsList) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            val list = noteContentState.value.filteredCategoryDetailsList
            if (list.isNotEmpty() && page in list.indices) {
                onChangeCategoryId(list[page].id)
                viewModel.onActions(NoteScreenActions.UpdateSelectedCategoryDetails(list[page]))
            }
        }
    }

    LaunchedEffect(viewModel.noteUiAction) {
        viewModel.noteUiAction.collect { navigationEffect ->
            when(navigationEffect) {
                is NoteUiAction.None -> {}
                is NoteUiAction.ChoiceLabelSheet -> {
                    showChoiceLabelSheetState = true
                }
                is NoteUiAction.EditNote -> {
                    onNavigationRoutes(NavigationRoutes.EditNoteScreenRoute(navigationEffect.noteId))
                }
                is NoteUiAction.ShowDeleteSnackbar -> {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = deleteNoteSnackbarTitle,
                        actionLabel = cancelNoteCategorySnackbarTitle,
                        duration = SnackbarDuration.Long
                    )
                    when(snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onActions(NoteScreenActions.NoteActions(NoteAction.RestoreNote(navigationEffect.noteDetails)))
                        }
                        SnackbarResult.Dismissed -> {} /*snackbarHostState.showSnackbar(
                            message = moveToBasketDeleteNoteSnackbarTitle,
                            duration = SnackbarDuration.Short


                        )

                        */
                    }
                }
            }
        }
    }

    NoteBody(
        pagerState = pagerState,
        snackbarHostState = snackbarHostState,
        noteUiState = noteUiState.value,
        noteContentState = noteContentState.value,
        onNavigationRoutes = onNavigationRoutes,
        onActions = viewModel::onActions
    )

    if (showChoiceLabelSheetState) {
        ChoiceLabelSheet(
            sheetState = choiceLabelSheetState,
            labelDetailsList = labelDetailsListState.value.labelDetailsList,
            onClick = {
                viewModel.onActions(
                    NoteScreenActions.EditLabel(it)
                )
            }
        ) {
            scope.launch { choiceLabelSheetState.hide() }.invokeOnCompletion {
                showChoiceLabelSheetState = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteBody(
    pagerState: PagerState,
    snackbarHostState: SnackbarHostState,
    noteUiState: NoteUiState,
    noteContentState: NoteContentState,
    onNavigationRoutes: (NavigationRoutes) -> Unit,
    onActions: (NoteScreenActions) -> Unit
) {

    val successSnackbarTitle = stringResource(R.string.success_title)

    val scope: CoroutineScope = rememberCoroutineScope()
    val noteActionsSheetState: SheetState = rememberModalBottomSheetState()
    var showNoteActionsSheetState by rememberSaveable { mutableStateOf(false) }
    val categoryActionsSheetState: SheetState = rememberModalBottomSheetState()
    var showCategoryActionsSheetState by rememberSaveable { mutableStateOf(false) }

    val safeSelectedIndex = pagerState.currentPage
        .coerceAtMost(noteContentState.filteredCategoryDetailsList.size - 1)
        .coerceAtLeast(0)


    val onCardClicked: (String) -> Unit = remember(onNavigationRoutes) {
        { id -> onNavigationRoutes(NavigationRoutes.DetailsScreenRoute(id)) }
    }
    val onActionsClicked: (NoteDetails) -> Unit = remember(onActions) {
        { noteDetails ->
            scope.launch {
                onActions(NoteScreenActions.UpdateSelectedNoteDetails(noteDetails))
                showNoteActionsSheetState = true
            }
        }
    }
    // Сейчас индекс сбрасывается на 0, но потом надо переделать с сохранением (ждать пока загрузится список)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PrimaryScrollableTabRow(selectedTabIndex = safeSelectedIndex) {
            noteContentState.filteredCategoryDetailsList.forEachIndexed { index, details ->
                Tab(
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = details.title.take(25),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (details.fix) {
                                Icon(
                                    painter = painterResource(R.drawable.keep),
                                    contentDescription = null
                                )
                            }
                            if (details.highlight) {
                                Box(
                                    Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .combinedClickable(
                            onClick = {},
                            onLongClick = { showCategoryActionsSheetState = true }
                        )
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                    onClick = { onNavigationRoutes(NavigationRoutes.EntryCategoryScreenRoute) }
                ) { Text(stringResource(R.string.add_title)) }
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
        ) { pager ->
            val noteContentContainer = remember(pager, noteContentState) {
                val currentCategory = noteContentState.filteredCategoryDetailsList.getOrNull(pager)
                noteContentState.filteredNoteDetailsListByCategory[currentCategory?.id ?: ""] ?: NoteContentContainer()
            }


            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    key = { it.lazyKey },
                    items = noteContentContainer.noteDetailsList
                ) { noteDetails ->
                    NoteDetailsCard(
                        modifier = Modifier.animateItem(),
                        noteDetails = noteDetails,
                        onClickCard = onCardClicked,
                        onClickActions = { onActionsClicked(noteDetails) }
                    )
                }
                item {
                    AnimatedVisibility(noteContentContainer.noteDetailsListWhoDone.isNotEmpty()) {
                        Text(
                            text = "Выполненные",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                items(
                    key = { it.lazyKey },
                    items = noteContentContainer.noteDetailsListWhoDone
                ) { noteDetails ->
                    NoteDetailsCard(
                        modifier = Modifier.animateItem(),
                        noteDetails = noteDetails,
                        onClickCard = onCardClicked,
                        onClickActions = { onActionsClicked(noteDetails) }
                    )
                }
                item {
                    Box(
                        modifier = Modifier.height(60.dp)
                    )
                }
            }
        }
    }

    if (showNoteActionsSheetState) {
        NoteActionsSheet(
            sheetState = noteActionsSheetState,
            noteDetails = noteUiState.selectedNoteDetails,
            onActions = { onActions(NoteScreenActions.NoteActions(it)) }
        ) {
            scope.launch { noteActionsSheetState.hide() }.invokeOnCompletion {
                showNoteActionsSheetState = false
            }
        }
    }
    if (showCategoryActionsSheetState) {
        CategoryActionsSheet(
            sheetState = categoryActionsSheetState,
            categoryDetails = noteUiState.selectedCategoryDetails,
            onActions = { onActions(NoteScreenActions.CategoryActions(it)) }
        ) {
            scope.launch { noteActionsSheetState.hide() }.invokeOnCompletion {
                showCategoryActionsSheetState = false
            }
        }
    }

    when (noteUiState.flintActions) {
        is FlintActions.Default -> {}
        is FlintActions.Success -> {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = successSnackbarTitle,
                    duration = SnackbarDuration.Short
                )
            }
        }

        is FlintActions.Loading -> DefaultLoadingDialog()
        is FlintActions.Error -> {
            ErrorDialog(
                message = noteUiState.flintActions.message
            ) { onActions(NoteScreenActions.DismissError) }
        }
    }
}