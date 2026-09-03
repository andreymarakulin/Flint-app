package ru.andmar.flint.features.account.domain.usecase

import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.account.domain.model.AuthDetails

class SignInUseCase(private val accountRepository: AccountRepository) {

    suspend fun signIn(authDetails: AuthDetails) = runCatching {
        accountRepository.signIn(authDetails)
    }
}