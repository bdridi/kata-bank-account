package io.shodo.application

import io.shodo.domain.DepositUseCaseApi
import io.shodo.domain.GenerateStatementUseCaseApi
import io.shodo.domain.WithdrawalUseCaseApi
import io.shodo.domain.model.Account
import org.joda.money.Money
import java.lang.StringBuilder

class AccountConsole(
    private val depositUseCaseApi: DepositUseCaseApi,
    private val withdrawalUseCaseApi: WithdrawalUseCaseApi,
    private val generateStatementUseCaseApi: GenerateStatementUseCaseApi
){
    fun printStatement(account: Account): String{
        val statement = StringBuilder()
        statement.appendLine("transaction | date | amount | balance")
        generateStatementUseCaseApi.getStatement(account)
            .forEach {
                statement.appendLine("${it.transactionType.name} | ${it.dateTime} | ${it.amount} | ${it.balance}")
            }
        return statement.toString()
    }

    fun deposit(account: Account, amount: Money){
        depositUseCaseApi.deposit(account, amount)
    }

    fun withdrawal(account: Account, amount: Money){
        withdrawalUseCaseApi.withdrawal(account, amount)
    }
}