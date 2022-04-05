package io.shodo.domain

import io.shodo.domain.model.Transaction

interface TransactionRepositorySpi {
    fun createTransaction(transaction: Transaction)
    fun findTransactionByAccountNumber(accountNumber: String): List<Transaction>

}