package io.shodo.domain

import io.shodo.domain.model.Account
import io.shodo.domain.model.StatementEntry

interface GenerateStatementUseCaseApi {
    fun getStatement(account: Account): List<StatementEntry>
}