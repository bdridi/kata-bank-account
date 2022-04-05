package io.shodo.domain

import io.shodo.domain.model.Account
import io.shodo.domain.model.Transaction
import io.shodo.domain.model.TransactionType
import org.joda.money.Money
import java.lang.IllegalArgumentException

class DepositUseCase(
    private val transactionRepositorySpi: TransactionRepositorySpi,
    private val clock: Clock
) : DepositUseCaseApi {
    override fun deposit(account: Account, amount: Money) {
        if (amount.isNegativeOrZero)
            throw IllegalArgumentException("cannot deposit a negative or zero amount")
        if (account.currency != amount.currencyUnit)
            throw IllegalArgumentException("cannot deposit an amount in different account currency")

        transactionRepositorySpi.createTransaction(Transaction(
            type = TransactionType.DEPOSIT,
            amount = amount,
            accountNumber = account.number,
            dateTime = clock.getLocalDateTime()
        ))
    }
}