package ru.andmar.flint.features.account.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.andmar.flint.core.data.FlintDao
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.data.model.UserItem
import ru.andmar.flint.features.account.domain.model.AuthDetails

class AccountRepository(private val flintDao: FlintDao) {
    fun getAuthState(): Flow<FlintActions> = flowOf(FlintActions.Success)
    suspend fun signIn(authDetails: AuthDetails) {
        TODO("Not yet implemented")
    }

    suspend fun createUser(authDetails: AuthDetails) {
        TODO("Not yet implemented")
    }

    suspend fun signOut() {
        TODO("Not yet implemented")
    }

    suspend fun createUser(userItem: UserItem) {
        TODO("Not yet implemented")
    }
}