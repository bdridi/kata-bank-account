package io.shodo.domain.model

import org.joda.money.Money
import java.time.LocalDateTime

data class StatementEntry(val dateTime: LocalDateTime, val transactionType: TransactionType, val amount: Money, val balance: Money)