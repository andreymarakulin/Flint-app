package ru.andmar.flint.features.account.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.data.model.UserItem
import ru.andmar.flint.features.account.domain.model.AuthDetails
import ru.andmar.flint.features.settings.domain.UserDetails

class AccountRepository(private val flintDao: FlintDao) {
    fun getAuthState(): Flow<FlintActions> = flowOf(FlintActions.Success)

    fun isAuth(): Boolean {
        return true
    }
    suspend fun signIn(authDetails: AuthDetails) {}

    suspend fun signUp(authDetails: AuthDetails) {}

    suspend fun updateEmail(email: String) {}
    suspend fun updatePassword(password: String) {}

    suspend fun signOut() {}

    suspend fun createUser() {}

    suspend fun getUser(): UserDetails = UserDetails()
}