package ru.andmar.flint.navigation

import kotlinx.serialization.Serializable


@Serializable sealed interface NavigationRoutes {

    @Serializable object HomeScreenRoute: NavigationRoutes
    @Serializable object EntryCategoryScreenRoute: NavigationRoutes
    @Serializable data class EditCategoryScreenRoute(val categoryId: String): NavigationRoutes
    @Serializable data class EntryNoteScreenRoute(val categoryId: String): NavigationRoutes
    @Serializable data class EditNoteScreenRoute(val noteId: String): NavigationRoutes
    @Serializable data class DetailsScreenRoute(val noteId: String): NavigationRoutes
    @Serializable data class EntryTodoScreenRoute(val noteId: String): NavigationRoutes
    @Serializable data class EditTodoScreenRoute(val todoId: String): NavigationRoutes
    @Serializable object EntryLabelScreenRoute: NavigationRoutes
    @Serializable data class EditLabelScreenRoute(val labelId: String): NavigationRoutes
    @Serializable object EntryReminderScreenRoute: NavigationRoutes
    @Serializable data class EditReminderScreenRoute(val reminderId: String): NavigationRoutes
    @Serializable object AccountScreenRoute: NavigationRoutes
    @Serializable object SignInScreenRoute: NavigationRoutes
    @Serializable object SignUpScreenRoute: NavigationRoutes
    @Serializable object SettingsScreenRoute: NavigationRoutes
    @Serializable object AboutAppScreenRoute: NavigationRoutes
}
