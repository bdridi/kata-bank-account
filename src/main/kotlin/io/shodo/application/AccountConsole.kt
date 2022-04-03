package io.shodo.application

import io.shodo.domain.AccountServiceApi
import io.shodo.domain.model.Account
import org.joda.money.Money
import java.lang.StringBuilder

class AccountConsole(
    private val accountServiceApi: AccountServiceApi
){
    fun printStatement(account: Account): String{
        val statement = StringBuilder()
        statement.appendLine("transaction | date | amount | balance")
        accountServiceApi.getStatement(account)
            .forEach {
                statement.appendLine("${it.transactionType.name} | ${it.dateTime} | ${it.amount} | ${it.balance}")
            }
        return statement.toString()
    }

    fun deposit(account: Account, amount: Money){
        accountServiceApi.deposit(account, amount)
    }

    fun withdrawal(account: Account, amount: Money){
        accountServiceApi.withdrawal(account, amount)
    }
}