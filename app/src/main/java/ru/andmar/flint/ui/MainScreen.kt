package ru.andmar.flint.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.andmar.flint.navigation.NavigationRoutes
import ru.andmar.flint.R
import ru.andmar.flint.core.ui.ModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultModalSheetItem
import ru.andmar.flint.core.ui.components.DefaultTopAppBar
import ru.andmar.flint.features.category.ui.home.CategoryScreen
import ru.andmar.flint.features.note.ui.home.NoteScreen
import ru.andmar.flint.features.reminder.ui.home.ReminderScreen
import ru.andmar.flint.features.settings.ui.SettingsScreen
import ru.andmar.flint.features.todo.ui.home.TodoScreen
import ru.andmar.flint.navigation.MainScreenNavigationRoutes


data class FlintNavigationBarItem(
    val title: Int,
    val icon: Int,
    val route: MainScreenNavigationRoutes
)

val navigationBarItemList = listOf(
    FlintNavigationBarItem(
        title = R.string.notes_screen_title,
        icon = R.drawable.notes,
        route = MainScreenNavigationRoutes.NoteScreenRoute
    ),
    FlintNavigationBarItem(
        title = R.string.todos_screen_title,
        icon = R.drawable.task_alt,
        route = MainScreenNavigationRoutes.TodoScreenRoute
    ),
    FlintNavigationBarItem(
        title = R.string.categories_screen_title,
        icon = R.drawable.category,
        route = MainScreenNavigationRoutes.CategoryScreenRoute
    ),
    /*
    FlintNavigationBarItem(
        title = R.string.reminders_screen_title,
        icon = R.drawable.reminder,
        route = MainScreenNavigationRoutes.ReminderScreenRoute
    )

     */
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigationRoutes: (NavigationRoutes) -> Unit
) {

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedNavigationBarIndex by rememberSaveable { mutableIntStateOf(0) }
    var categoryId by rememberSaveable { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val homeMenuSheetState = rememberModalBottomSheetState()
    var isShowHomeMenuSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(navigationBarItemList[selectedNavigationBarIndex].title),
                //navIcon = R.drawable.label,
                navDes = null,
                onNavIcon = {},
                //actionsIcon = R.drawable.more_vert,
                actionsDes = null,
                onActions = { isShowHomeMenuSheet = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (selectedNavigationBarIndex) {
                        0 -> {
                            onNavigationRoutes(NavigationRoutes.EntryNoteScreenRoute(categoryId))
                        }

                        1 -> {
                            onNavigationRoutes(NavigationRoutes.EntryTodoScreenRoute(""))
                        }

                        2 -> {
                            onNavigationRoutes(NavigationRoutes.EntryCategoryScreenRoute)
                        }

                        3 -> {
                            onNavigationRoutes(NavigationRoutes.EntryReminderScreenRoute)
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null
                )
            }

        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
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
                        label = { Text(stringResource(destination.title)) }
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
            ) { }
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
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        homeMenuSheetItems(onNavigationRoutes).forEach { item ->
            DefaultModalSheetItem(
                title = item.title,
                icon = item.icon
            ) {
                item.onClick()
                onDismiss()
            }
        }
    }
}

fun homeMenuSheetItems(onNavigationRoutes: (NavigationRoutes) -> Unit): List<ModalSheetItem> = listOf(
    ModalSheetItem(
        title = R.string.account_title,
        icon = R.drawable.account_circle
    ) {  },
    ModalSheetItem(
        title = R.string.archive_title,
        icon = R.drawable.archive
    ) {  },
    ModalSheetItem(
        title = R.string.basket_title,
        icon = R.drawable.delete
    ) {  },
    ModalSheetItem(
        title = R.string.settings_screen_title,
        icon = R.drawable.settings
    ) {  },
    ModalSheetItem(
        title = R.string.about_app_title,
        icon = R.drawable.info
    ) {  }
)