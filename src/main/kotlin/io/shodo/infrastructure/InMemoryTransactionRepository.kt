package io.shodo.infrastructure

import io.shodo.domain.TransactionRepositorySpi
import io.shodo.domain.model.Transaction

class InMemoryTransactionRepository: TransactionRepositorySpi {
    private val transactions = mutableListOf<Transaction>()

    override fun createTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }

    override fun findTransactionByAccountNumber(accountNumber: String): List<Transaction> {
       return transactions.filter { it.accountNumber == accountNumber }
    }
}