package com.andmar.flint.firebase

import com.andmar.flint.FlintActions
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface DefaultAuthClient {

    fun getCurrentUser(): FirebaseUser?
    fun getAuthState(): Flow<FlintActions>
    fun signIn(authItem: AuthItem, onFlintActions: (FlintActions) -> Unit)
    fun createUser(authItem: AuthItem, onFlintActions: (FlintActions) -> Unit)
    fun signOut(onFlintActions: (FlintActions) -> Unit)
}