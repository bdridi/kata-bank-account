package io.shodo.domain.model

import org.joda.money.CurrencyUnit

data class Account(val number: String, val owner: String, val currency: CurrencyUnit)