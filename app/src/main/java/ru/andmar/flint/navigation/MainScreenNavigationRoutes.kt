package ru.andmar.flint.navigation

import kotlinx.serialization.Serializable

@Serializable sealed interface MainScreenNavigationRoutes {
    @Serializable object CategoryScreenRoute: MainScreenNavigationRoutes
    @Serializable object NoteScreenRoute: MainScreenNavigationRoutes
    @Serializable object TodoScreenRoute: MainScreenNavigationRoutes
    @Serializable object ReminderScreenRoute: MainScreenNavigationRoutes
}
