package ru.andmar.flint.core.data

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.data.model.AuthItem

interface DefaultAuthClient {

    fun getCurrentUser(): FirebaseUser?
    fun getAuthState(): Flow<FlintActions>
    suspend fun signIn(authItem: AuthItem)
    suspend fun createUser(authItem: AuthItem)
    suspend fun signOut()
}