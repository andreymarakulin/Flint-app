package com.andmar.flint

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthClient(private val auth: FirebaseAuth): DefaultAuthClient {

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getAuthState(): Flow<FlintActions> = callbackFlow {
        trySend(FlintActions.Loading)
        val authState = FirebaseAuth.AuthStateListener {
            trySend(
                if (it.currentUser != null) {
                    FlintActions.Success
                } else FlintActions.Error("Not user")
            )
        }
        auth.addAuthStateListener(authState)
        awaitClose { auth.removeAuthStateListener(authState) }
    }

    override suspend fun signIn(authItem: AuthItem) {
        auth.signInWithEmailAndPassword(
            authItem.email,
            authItem.password
        ).await()
    }

    override suspend fun createUser(authItem: AuthItem) {
        auth.createUserWithEmailAndPassword(
            authItem.email,
            authItem.password
        ).await()
    }

    override suspend fun signOut() {
            auth.signOut()
    }
}