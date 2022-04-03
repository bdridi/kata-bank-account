package io.shodo.domain

import io.shodo.domain.model.Account
import io.shodo.domain.model.StatementEntry
import org.joda.money.Money

interface AccountServiceApi {
    fun deposit(account: Account, amount: Money)
    fun withdrawal(account: Account, amount: Money)
    fun getStatement(account: Account): List<StatementEntry>

}