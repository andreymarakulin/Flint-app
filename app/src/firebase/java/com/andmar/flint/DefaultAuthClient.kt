package com.andmar.flint

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface DefaultAuthClient {

    fun getCurrentUser(): FirebaseUser?
    fun getAuthState(): Flow<FlintActions>
    suspend fun signIn(authItem: AuthItem)
    suspend fun createUser(authItem: AuthItem)
    suspend fun signOut()
}