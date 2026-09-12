package ru.andmar.flint.features.account.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import ru.andmar.flint.core.data.DefaultAuthClient
import ru.andmar.flint.core.data.DefaultFirestoreClient
import ru.andmar.flint.core.ui.FlintActions
import ru.andmar.flint.features.account.data.toAuthItem
import ru.andmar.flint.features.account.data.toUserDetails
import ru.andmar.flint.features.account.domain.model.AuthDetails
import ru.andmar.flint.features.settings.domain.UserDetails

class AccountRepository(
    private val authClient: DefaultAuthClient,
    private val firestoreClient: DefaultFirestoreClient
) {

    fun isAuth(): Boolean {
        return authClient.getCurrentUser() != null
    }

    fun getCurrentUser(): FirebaseUser? =
        authClient.getCurrentUser()

    fun getAuthState(): Flow<FlintActions> =
        authClient.getAuthState()

    suspend fun signIn(authDetails: AuthDetails) = authClient.signIn(authDetails.toAuthItem())

    suspend fun signUp(authDetails: AuthDetails) = authClient.createUser(authDetails.toAuthItem())

    suspend fun updateEmail(email: String) = authClient.updateEmail(email)
    suspend fun updatePassword(password: String) = authClient.updateEmail(password)

    suspend fun signOut() = authClient.signOut()




    suspend fun createUser() = firestoreClient.setUserItem()

    suspend fun getUser(): UserDetails = firestoreClient.getUserItem().toUserDetails()
}