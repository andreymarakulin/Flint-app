package ru.andmar.flint.features.account.data

import ru.andmar.flint.features.account.data.model.AuthItem
import ru.andmar.flint.features.account.domain.model.AuthDetails

fun AuthDetails.toAuthItem(): AuthItem = AuthItem(
    email = email,
    password = password
)

fun AuthItem.toAuthDetails(): AuthDetails = AuthDetails(
    email = email,
    password = password
)