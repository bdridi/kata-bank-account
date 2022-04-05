package io.shodo.domain

import io.shodo.domain.model.Account
import org.joda.money.Money

interface DepositUseCaseApi {
    fun deposit(account: Account, amount: Money)
}