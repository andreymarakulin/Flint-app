package ru.andmar.flint.features.account.domain.usecase

import ru.andmar.flint.features.account.data.repository.AccountRepository

class ChangeEmailUseCase(private val accountRepository: AccountRepository) {

    suspend fun changeEmail(email: String) = runCatching {
        accountRepository.updateEmail(email)
    }
}