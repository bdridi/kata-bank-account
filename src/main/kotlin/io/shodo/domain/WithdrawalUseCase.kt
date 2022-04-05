package io.shodo.domain

import io.shodo.domain.model.Account
import io.shodo.domain.model.Transaction
import io.shodo.domain.model.TransactionType
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException

class WithdrawalUseCase(
    private val transactionRepositorySpi: TransactionRepositorySpi,
    private val clock: Clock
): WithdrawalUseCaseApi {

    override fun withdrawal(account: Account, amount: Money) {
        if(amount.isNegativeOrZero)
            throw IllegalArgumentException("cannot deposit a negative or zero amount")
        if(account.currency != amount.currencyUnit)
            throw IllegalArgumentException("cannot deposit an amount in different account currency")
        val transactions = transactionRepositorySpi.findTransactionByAccountNumber(accountNumber = account.number)
        val currentBalance = calculateBalance(transactions, account.currency)
        if(amount.isGreaterThan(currentBalance))
            throw UnsupportedOperationException("cannot withdrawal amount, balance not sufficient")
        transactionRepositorySpi.createTransaction(Transaction(
            type = TransactionType.WITHDRAWAL,
            amount = amount,
            accountNumber = account.number,
            dateTime = clock.getLocalDateTime()
        ))
    }

    private fun calculateBalance(transactions: List<Transaction>, currency: CurrencyUnit): Money {
        return transactions.fold(Money.zero(currency)){ acc, transaction ->  if(transaction.type == TransactionType.DEPOSIT) acc.plus(transaction.amount) else acc.minus(transaction.amount)}
    }
}