package io.shodo.domain

import io.shodo.domain.model.Account
import io.shodo.domain.model.StatementEntry
import io.shodo.domain.model.TransactionType
import org.joda.money.Money

class GenerateStatementUseCase(
    private val transactionRepositorySpi: TransactionRepositorySpi,
) : GenerateStatementUseCaseApi {

    override fun getStatement(account: Account): List<StatementEntry> {
        var runningBalance = Money.zero(account.currency)
        return transactionRepositorySpi.findTransactionByAccountNumber(accountNumber = account.number)
            .sortedBy { it.dateTime }
            .map {
                runningBalance = if (it.type == TransactionType.DEPOSIT)
                    runningBalance.plus(it.amount)
                else runningBalance.minus(it.amount)
                StatementEntry(transactionType = it.type,
                    dateTime = it.dateTime,
                    amount = it.amount,
                    balance = runningBalance)
            }

    }
}