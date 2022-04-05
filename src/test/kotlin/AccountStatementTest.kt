import io.shodo.domain.*
import io.shodo.domain.model.Account
import io.shodo.domain.model.StatementEntry
import io.shodo.domain.model.Transaction
import io.shodo.domain.model.TransactionType
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import java.util.*

class AccountStatementTest {

    private val transactionRepositorySpi: TransactionRepositorySpi = mock { }

    companion object {
        private val mockTransactionDateTime = LocalDateTime.parse("2022-01-01T15:30")
        private val accountNumber = UUID.randomUUID().toString()
        private val account = Account(number = accountNumber, owner = "Chuck Norris", currency = CurrencyUnit.EUR)
    }

    class TestClock : Clock() {
        override fun getLocalDateTime(): LocalDateTime {
            return mockTransactionDateTime
        }
    }

    private val generateStatementUseCaseApi: GenerateStatementUseCaseApi =
        GenerateStatementUseCase(transactionRepositorySpi = transactionRepositorySpi)

    @Test
    internal fun `account statement of empty account should return empty `() {
        // GIVEN
        given(transactionRepositorySpi.findTransactionByAccountNumber(accountNumber)).willReturn(listOf())
        // WHEN
        val statement = generateStatementUseCaseApi.getStatement(account)

        // THEN
        assertThat(statement.size).isEqualTo(0)
    }


    @Test
    internal fun `account statement of active account should return list of statement line `() {
        // GIVEN
        val dateTimeFirstTransaction = LocalDateTime.parse("2022-01-01T15:30")
        val dateTimeSecondTransaction = LocalDateTime.parse("2022-01-02T15:30")
        val dateTimeThirdTransaction = LocalDateTime.parse("2022-01-03T15:30")

        given(transactionRepositorySpi.findTransactionByAccountNumber(accountNumber)).willReturn(
            listOf(Transaction(
                type = TransactionType.DEPOSIT, amount = Money.parse("EUR 500"),
                dateTime = dateTimeFirstTransaction,
                accountNumber = accountNumber
            ),
                Transaction(
                    type = TransactionType.WITHDRAWAL, amount = Money.parse("EUR 300"),
                    dateTime = dateTimeSecondTransaction, accountNumber = accountNumber
                ),
                Transaction(
                    type = TransactionType.DEPOSIT, amount = Money.parse("EUR 150"),
                    dateTime = dateTimeThirdTransaction, accountNumber = accountNumber
                )
            ))
        // WHEN
        val statement = generateStatementUseCaseApi.getStatement(account)

        // THEN
        val expectedStatement = listOf(
            StatementEntry(transactionType = TransactionType.DEPOSIT,
                dateTime = dateTimeFirstTransaction,
                amount = Money.parse("EUR 500"),
                balance = Money.parse("EUR 500")),
            StatementEntry(transactionType = TransactionType.WITHDRAWAL,
                dateTime = dateTimeSecondTransaction,
                amount = Money.parse("EUR 300"),
                balance = Money.parse("EUR 200")),
            StatementEntry(transactionType = TransactionType.DEPOSIT,
                dateTime = dateTimeThirdTransaction,
                amount = Money.parse("EUR 150"),
                balance = Money.parse("EUR 350"))
        )
        assertThat(statement).isEqualTo(expectedStatement)
    }
}