package io.shodo.domain.model

import org.joda.money.Money
import java.time.LocalDateTime

data class Transaction(val type: TransactionType, val amount: Money, val dateTime: LocalDateTime, val accountNumber: String)
