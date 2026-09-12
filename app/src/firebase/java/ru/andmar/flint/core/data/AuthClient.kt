package ru.andmar.flint.core.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.data.model.AuthItem

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

    override suspend fun updateEmail(email: String) {
        val user = auth.currentUser

        //val credential = EmailAuthProvider.getCredential(email, currentPassword)

        // Подтверждаем личность через корутины
        //user.reauthenticate(credential).await()


        user?.verifyBeforeUpdateEmail(email)?.await()
    }

    override suspend fun updatePassword(password: String) {
        val user = auth.currentUser

       // val credential = EmailAuthProvider.getCredential(email, currentPassword)

        // Подтверждаем личность через корутины
        //user.reauthenticate(credential).await()


        user?.updatePassword(password)?.await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}