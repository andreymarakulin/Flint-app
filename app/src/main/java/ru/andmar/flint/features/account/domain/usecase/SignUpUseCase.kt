package ru.andmar.flint.features.account.domain.usecase

import ru.andmar.flint.features.account.data.model.UserItem
import ru.andmar.flint.features.account.data.repository.AccountRepository
import ru.andmar.flint.features.account.domain.model.AuthDetails

class SignUpUseCase(private val accountRepository: AccountRepository) {


    suspend fun createUser(authDetails: AuthDetails) = runCatching {
        accountRepository.createUser(authDetails)
    }

    suspend fun createUser() = runCatching {
        accountRepository.createUser()
    }
}