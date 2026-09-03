package ru.andmar.flint.features.account.domain.usecase

import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.account.domain.model.AuthDetails
import ru.andmar.flint.features.settings.domain.UserDetails

class SignUpUseCase(private val accountRepository: AccountRepository) {


    suspend fun createAccount(authDetails: AuthDetails) = runCatching {
        accountRepository.signUp(authDetails)
    }

    suspend fun createUser() = runCatching {
        accountRepository.createUser()
    }
}