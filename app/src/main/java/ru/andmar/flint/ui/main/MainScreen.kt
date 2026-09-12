package ru.andmar.flint.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.andmar.flint.BuildConfig
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.sheet.DefaultMenuSheet
import ru.andmar.flint.features.category.ui.home.CategoryScreen
import ru.andmar.flint.features.note.ui.home.NoteScreen
import ru.andmar.flint.features.reminder.ui.home.ReminderScreen
import ru.andmar.flint.features.todo.ui.home.TodoScreen
import ru.andmar.flint.navigation.MainScreenNavigationRoutes
import ru.andmar.flint.navigation.NavigationRoutes


data class FlintNavigationBarItem(
    val title: Int,
    val icon: Int,
    val route: MainScreenNavigationRoutes,
    val entryRoute: NavigationRoutes
)

fun navigationBarItemList(categoryId: String) = listOf(
    FlintNavigationBarItem(
        title = R.string.notes_screen_title,
        icon = R.drawable.notes,
        route = MainScreenNavigationRoutes.NoteScreenRoute,
        entryRoute = NavigationRoutes.EntryNoteScreenRoute(categoryId)
    ),
    FlintNavigationBarItem(
        title = R.string.todos_screen_title,
        icon = R.drawable.task_alt,
        route = MainScreenNavigationRoutes.TodoScreenRoute,
        entryRoute = NavigationRoutes.EntryTodoScreenRoute("")
    ),
    FlintNavigationBarItem(
        title = R.string.categories_screen_title,
        icon = R.drawable.category,
        route = MainScreenNavigationRoutes.CategoryScreenRoute,
        entryRoute = NavigationRoutes.EntryCategoryScreenRoute
    ),
    /*
    FlintNavigationBarItem(
        title = R.string.reminders_screen_title,
        icon = R.drawable.reminder,
        route = MainScreenNavigationRoutes.ReminderScreenRoute,
        entryRoute = NavigationRoutes.EntryReminderScreenRoute
    )

     */
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = koinViewModel(),
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val labelDetailsListState = viewModel.choiceLabelDetailsListState.collectAsStateWithLifecycle()
    val mainUiState = viewModel.mainUiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedNavigationBarIndex by rememberSaveable { mutableIntStateOf(0) }
    var categoryId by rememberSaveable { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val homeMenuSheetState = rememberModalBottomSheetState()
    var isShowHomeMenuSheet by rememberSaveable { mutableStateOf(false) }

    val navigationBarItemList = navigationBarItemList(categoryId)
    val currentNavigationBarItem = navigationBarItemList[selectedNavigationBarIndex]

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    /*
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),

                     */
                    title = {
                        AnimatedContent(currentNavigationBarItem.title) { title ->
                            Text(
                                text = stringResource(title),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { onNavigationRoutes(NavigationRoutes.LabelScreenRoute) }
                        ) { Icon(painterResource(R.drawable.label), null) }
                    },
                    actions = {
                        IconButton(
                            onClick = { isShowHomeMenuSheet = true }
                        ) { Icon(painterResource(R.drawable.more_vert), null) }
                    },
                    scrollBehavior = scrollBehavior
                )
                AnimatedVisibility(labelDetailsListState.value.labelDetailsList.isNotEmpty()) {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(labelDetailsListState.value.labelDetailsList) { labelDetails ->
                            FilterChip(
                                selected = true,
                                onClick = { viewModel.onActions(MainScreenActions.RemoveChoiceLabelDetails(labelDetails)) },
                                label = { Text(labelDetails.title) },
                                trailingIcon = { Icon(painterResource(R.drawable.close), null) },
                                modifier = Modifier.padding(horizontal = 5.dp)
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigationRoutes(currentNavigationBarItem.entryRoute) },
            ) { Icon(painterResource(R.drawable.add),null) }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                windowInsets = NavigationBarDefaults.windowInsets,
                //containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                navigationBarItemList.forEachIndexed { index, destination ->
                    val isSelected = currentDestination?.hasRoute(destination.route::class) == true

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(destination.route)
                                selectedNavigationBarIndex = index
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(destination.title),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
    ) { contentPadding ->
        MainScreenNavHost(
            contentPaddingValues = contentPadding,
            snackbarHostState = snackbarHostState,
            navController = navController,
            startDestination = MainScreenNavigationRoutes.NoteScreenRoute,
            onChangeCategoryId = { categoryId = it }
        ) { onNavigationRoutes(it) }
    }

    if (isShowHomeMenuSheet) {
        MainScreenMenuSheet(
            sheetState = homeMenuSheetState,
            onNavigationRoutes = { onNavigationRoutes(it) }
        ) {
            scope.launch { homeMenuSheetState.hide() }.invokeOnCompletion {
                isShowHomeMenuSheet = false
            }
        }
    }
}
@Composable
fun MainScreenNavHost(
    contentPaddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    startDestination: MainScreenNavigationRoutes,
    onChangeCategoryId: (String) -> Unit,
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPaddingValues)
    ) {
        composable<MainScreenNavigationRoutes.CategoryScreenRoute> {
            CategoryScreen(
                snackbarHostState = snackbarHostState,
            ) { onNavigationRoutes(it) }
        }
        composable<MainScreenNavigationRoutes.NoteScreenRoute> {
            NoteScreen(
                snackbarHostState = snackbarHostState,
                onChangeCategoryId = { onChangeCategoryId(it) }
            ) { onNavigationRoutes(it) }
        }
        composable<MainScreenNavigationRoutes.TodoScreenRoute> {
            TodoScreen(
                snackbarHostState = snackbarHostState,
            ) { onNavigationRoutes(it) }
        }
        composable<MainScreenNavigationRoutes.ReminderScreenRoute> {
            ReminderScreen(
                snackbarHostState = snackbarHostState,
            ) { onNavigationRoutes(it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenMenuSheet(
    sheetState: SheetState,
    onNavigationRoutes: (NavigationRoutes) -> Unit,
    onDismiss: () -> Unit
) {
    DefaultMenuSheet(
        sheetState = sheetState,
        menuSheetItems = homeMenuSheetItems(onNavigationRoutes),
        onDismiss = onDismiss
    )
}

fun homeMenuSheetItems(onNavigationRoutes: (NavigationRoutes) -> Unit): List<ModalSheetItem> {
    return if (BuildConfig.FLAVOR == "firebase") {
        listOf(
            /*
            ModalSheetItem(
                title = R.string.account_title,
                icon = R.drawable.account_circle
            ) { },

             */
            ModalSheetItem(
                title = R.string.archive_title,
                icon = R.drawable.archive
            ) { onNavigationRoutes(NavigationRoutes.ArchiveScreenRoute) },
            /*
            ModalSheetItem(
                title = R.string.basket_title,
                icon = R.drawable.delete
            ) { onNavigationRoutes(NavigationRoutes.BasketScreenRoute) },
            ModalSheetItem(
                title = R.string.settings_screen_title,
                icon = R.drawable.settings
            ) { },
            ModalSheetItem(
                title = R.string.about_app_title,
                icon = R.drawable.info
            ) { }

             */
        )
    } else {
        listOf(
            ModalSheetItem(
                title = R.string.archive_title,
                icon = R.drawable.archive
            ) { onNavigationRoutes(NavigationRoutes.ArchiveScreenRoute) },
            /*
            ModalSheetItem(
                title = R.string.basket_title,
                icon = R.drawable.delete
            ) { onNavigationRoutes(NavigationRoutes.BasketScreenRoute) },
            ModalSheetItem(
                title = R.string.settings_screen_title,
                icon = R.drawable.settings
            ) { },
            ModalSheetItem(
                title = R.string.about_app_title,
                icon = R.drawable.info
            ) { }

             */
        )
    }
}