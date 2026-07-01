package com.andmar.flint.firebase

import com.andmar.flint.FlintActions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

    override fun signIn(
        authItem: AuthItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        auth.signInWithEmailAndPassword(
            authItem.email,
            authItem.password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onFlintActions(FlintActions.Success)
            } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
        }
    }

    override fun createUser(
        authItem: AuthItem,
        onFlintActions: (FlintActions) -> Unit
    ) {
        onFlintActions(FlintActions.Loading)
        auth.createUserWithEmailAndPassword(
            authItem.email,
            authItem.password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onFlintActions(FlintActions.Success)
            } else onFlintActions(FlintActions.Error(task.exception?.message.toString()))
        }
    }

    override fun signOut(onFlintActions: (FlintActions) -> Unit) {
        onFlintActions(FlintActions.Loading)
        try {
            auth.signOut()
            onFlintActions(FlintActions.Success)
        } catch (e: Exception) {
            onFlintActions(FlintActions.Error(e.message.toString()))
        }
    }
}