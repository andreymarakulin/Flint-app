package com.andmar.flint

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andmar.flint.ui.theme.account.AccountScreen
import com.andmar.flint.ui.theme.account.AccountScreenRoute
import com.andmar.flint.ui.theme.account.SignInScreen
import com.andmar.flint.ui.theme.account.SignInScreenRoute
import com.andmar.flint.ui.theme.account.SignUpScreen
import com.andmar.flint.ui.theme.account.SignUpScreenRoute
import com.andmar.flint.ui.theme.category.CategoryScreen
import com.andmar.flint.ui.theme.category.CategoryScreenRoute
import com.andmar.flint.ui.theme.category.EditCategoryScreen
import com.andmar.flint.ui.theme.category.EditCategoryScreenRoute
import com.andmar.flint.ui.theme.category.EntryCategoryScreen
import com.andmar.flint.ui.theme.category.EntryCategoryScreenRoute
import com.andmar.flint.ui.theme.details.DetailsScreen
import com.andmar.flint.ui.theme.details.DetailsScreenRoute
import com.andmar.flint.ui.theme.home.HomeScreen
import com.andmar.flint.ui.theme.home.HomeScreenRoute
import com.andmar.flint.ui.theme.label.EditLabelScreen
import com.andmar.flint.ui.theme.label.EditLabelScreenRoute
import com.andmar.flint.ui.theme.label.EntryLabelScreen
import com.andmar.flint.ui.theme.label.EntryLabelScreenRoute
import com.andmar.flint.ui.theme.label.LabelScreen
import com.andmar.flint.ui.theme.label.LabelScreenRoute
import com.andmar.flint.ui.theme.note.EditNoteScreen
import com.andmar.flint.ui.theme.note.EditNoteScreenRoute
import com.andmar.flint.ui.theme.note.EntryNoteActions
import com.andmar.flint.ui.theme.note.EntryNoteScreen
import com.andmar.flint.ui.theme.note.EntryNoteScreenRoute
import com.andmar.flint.ui.theme.reminder.EditReminderScreen
import com.andmar.flint.ui.theme.reminder.EditReminderScreenRoute
import com.andmar.flint.ui.theme.reminder.EntryReminderScreen
import com.andmar.flint.ui.theme.reminder.EntryReminderScreenRoute
import com.andmar.flint.ui.theme.reminder.ReminderScreen
import com.andmar.flint.ui.theme.reminder.ReminderScreenRoute
import com.andmar.flint.ui.theme.todo.EditTodoScreen
import com.andmar.flint.ui.theme.todo.EditTodoScreenRoute
import com.andmar.flint.ui.theme.todo.EntryTodoScreen
import com.andmar.flint.ui.theme.todo.EntryTodoScreenRoute
import com.andmar.flint.ui.theme.todo.TodoScreen
import com.andmar.flint.ui.theme.todo.TodoScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationClient(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute
    ) {

        composable<HomeScreenRoute> {
            HomeScreen(
                onNavSignIn = { navController.navigate(SignInScreenRoute) },
                onNavDetails = { navController.navigate(DetailsScreenRoute(it)) },
                onNavCategory = { navController.navigate(CategoryScreenRoute) },
                onNavEntryCategory = { navController.navigate(EntryCategoryScreenRoute) },
                onNavEntryNote = { navController.navigate(EntryNoteScreenRoute(it)) },
                onNavEditNote = { navController.navigate(EditNoteScreenRoute(it)) },
                onNavTodo = { navController.navigate(TodoScreenRoute) }
            )
        }
        //Category
        composable<CategoryScreenRoute> {
            CategoryScreen(
                onNavEditCategory = { navController.navigate(EditCategoryScreenRoute(it)) }
            ) { navController.navigateUp() }
        }
        composable<EntryCategoryScreenRoute> {
            EntryCategoryScreen() { navController.navigateUp() }
        }
        composable<EditCategoryScreenRoute> {
            EditCategoryScreen() { navController.navigateUp() }
        }
        //Note
        composable<EntryNoteScreenRoute> {
            EntryNoteScreen() { navController.navigateUp() }
        }
        composable<EditNoteScreenRoute> {
            EditNoteScreen() { navController.navigateUp() }
        }
        //Details
        composable<DetailsScreenRoute> {
            DetailsScreen(
                onNavEditNote = { navController.navigate(EditNoteScreenRoute(it)) },
                onNavEntryTodo = { navController.navigate(EntryTodoScreenRoute(it)) },
                onNavEditTodo = { navController.navigate(EditTodoScreenRoute(it)) }
            ) { navController.navigateUp() }
        }
        //Todo
        composable<TodoScreenRoute> {
            TodoScreen(
                onNavEntryTodo = { navController.navigate(EntryTodoScreenRoute) },
                onNavEditTodo = { navController.navigate(EditTodoScreenRoute(it)) }
            ) {  navController.navigateUp() }
        }
        composable<EntryTodoScreenRoute> {
            EntryTodoScreen { navController.navigateUp() }
        }
        composable<EditTodoScreenRoute> {
            EditTodoScreen { navController.navigateUp() }
        }
        //Label
        composable<LabelScreenRoute> {
            LabelScreen() { navController.navigateUp() }
        }
        composable<EntryLabelScreenRoute> {
            EntryLabelScreen() { navController.navigateUp() }
        }
        composable<EditLabelScreenRoute> {
            EditLabelScreen() { navController.navigateUp() }
        }

        //Reminder
        composable<ReminderScreenRoute> {
            ReminderScreen() { navController.navigateUp() }
        }
        composable<EntryReminderScreenRoute> {
            EntryReminderScreen() { navController.navigateUp() }
        }
        composable<EditReminderScreenRoute> {
            EditReminderScreen() { navController.navigateUp() }
        }

        //Account
        composable<AccountScreenRoute> {
            AccountScreen() { navController.navigateUp() }
        }
        composable<SignInScreenRoute> {
            SignInScreen(
                onNavSignUp = { navController.navigate(SignUpScreenRoute) },
                onNavHome = { navController.navigate(HomeScreenRoute) }
            ) { navController.navigateUp() }
        }
        composable<SignUpScreenRoute> {
            SignUpScreen { navController.navigateUp() }
        }
    }
}