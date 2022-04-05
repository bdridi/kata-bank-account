package io.shodo.domain

import io.shodo.domain.model.Account
import org.joda.money.Money

interface WithdrawalUseCaseApi {
    fun withdrawal(account: Account, amount: Money)
}