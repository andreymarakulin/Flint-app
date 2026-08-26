package ru.andmar.flint.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import ru.andmar.flint.features.account.ui.home.AccountScreen
import ru.andmar.flint.features.account.ui.signIn.SignInScreen
import ru.andmar.flint.features.account.ui.signUp.SignUpScreen
import ru.andmar.flint.features.category.ui.edit.EditCategoryScreen
import ru.andmar.flint.features.category.ui.entry.EntryCategoryScreen
import ru.andmar.flint.features.label.ui.edit.EditLabelScreen
import ru.andmar.flint.features.label.ui.entry.EntryLabelScreen
import ru.andmar.flint.features.note.ui.details.DetailsScreen
import ru.andmar.flint.features.note.ui.edit.EditNoteScreen
import ru.andmar.flint.features.note.ui.entry.EntryNoteScreen
import ru.andmar.flint.features.reminder.ui.edit.EditReminderScreen
import ru.andmar.flint.features.reminder.ui.entry.EntryReminderScreen
import ru.andmar.flint.features.settings.ui.AboutAppScreen
import ru.andmar.flint.features.settings.ui.SettingsScreen
import ru.andmar.flint.features.todo.ui.edit.EditTodoScreen
import ru.andmar.flint.features.todo.ui.entry.EntryTodoScreen
import ru.andmar.flint.ui.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationClient() {
    val navController: NavHostController = rememberNavController()
    val isAuthState = Firebase.auth.currentUser != null

    NavHost(
        navController = navController,
        startDestination = if (isAuthState) NavigationRoutes.HomeScreenRoute else NavigationRoutes.SignInScreenRoute
        /*
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }

         */



        /*
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                //initialOffsetX = { it } // появление из-за правой границы экрана
            ) + fadeIn(animationSpec = tween(400))
        },
        // 2. Анимация исчезновения старого экрана (уходит влево)
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                //targetOffsetX = { -it / 3 } // Уходит влево только на 1/3 экрана (эффект параллакса)
            ) + fadeOut(animationSpec = tween(400))
        },
        // 3. Анимация появления предыдущего экрана при нажатии "Назад" (выезжает слева)
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                //initialOffsetX = { -it / 3 }
            ) + fadeIn(animationSpec = tween(400))
        },
        // 4. Анимация исчезновения текущего экрана при нажатии "Назад" (уходит вправо)
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
               // targetOffsetX = { it }
            ) + fadeOut(animationSpec = tween(400))
        }

         */
    ) {

        composable<NavigationRoutes.HomeScreenRoute> {
            HomeScreen { navController.navigate(it) }
        }
        //Category
        composable<NavigationRoutes.EntryCategoryScreenRoute> {
            EntryCategoryScreen() { navController.navigateUp() }
        }
        composable<NavigationRoutes.EditCategoryScreenRoute> {
            EditCategoryScreen() { navController.navigateUp() }
        }
        //Note
        composable<NavigationRoutes.EntryNoteScreenRoute> {
            EntryNoteScreen() { navController.navigateUp() }
        }
        composable<NavigationRoutes.EditNoteScreenRoute> {
            EditNoteScreen(
                onNavigationRoutes = { navController.navigate(it) }
            ) { navController.navigateUp() }
        }
        //Details
        composable<NavigationRoutes.DetailsScreenRoute> {
            DetailsScreen(
                onNavigationRoutes = { navController.navigate(it) }
            ) { navController.navigateUp() }
        }
        //Todo
        composable<NavigationRoutes.EntryTodoScreenRoute> {
            EntryTodoScreen { navController.navigateUp() }
        }
        composable<NavigationRoutes.EditTodoScreenRoute> {
            EditTodoScreen { navController.navigateUp() }
        }
        //Label
        composable<NavigationRoutes.EntryLabelScreenRoute> {
            EntryLabelScreen() { navController.navigateUp() }
        }
        composable<NavigationRoutes.EditLabelScreenRoute> {
            EditLabelScreen() { navController.navigateUp() }
        }

        //Reminder
        composable<NavigationRoutes.EntryReminderScreenRoute> {
            EntryReminderScreen() { navController.navigateUp() }
        }
        composable<NavigationRoutes.EditReminderScreenRoute> {
            EditReminderScreen() { navController.navigateUp() }
        }

        //About
        composable<NavigationRoutes.AboutAppScreenRoute> {
            AboutAppScreen() { navController.navigateUp() }
        }

        //Account
        composable<NavigationRoutes.AccountScreenRoute> {
            AccountScreen() { navController.navigateUp() }
        }
        composable<NavigationRoutes.SignInScreenRoute> {
            SignInScreen { navController.navigate(it) }
        }
        composable<NavigationRoutes.SignUpScreenRoute> {
            SignUpScreen { navController.navigateUp() }
        }

        composable<NavigationRoutes.SettingsScreenRoute> {
            SettingsScreen { navController.navigateUp() }
        }

        //About app
        composable<NavigationRoutes.AboutAppScreenRoute> {
            AboutAppScreen {  }
        }
    }
}