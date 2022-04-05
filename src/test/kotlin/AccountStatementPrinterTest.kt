import io.shodo.application.AccountConsole
import io.shodo.domain.*
import io.shodo.domain.model.Account
import io.shodo.domain.model.StatementEntry
import io.shodo.domain.model.TransactionType
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import java.util.*

class AccountStatementPrinterTest {

    companion object {
        private val accountNumber = UUID.randomUUID().toString()
        private val account = Account(number = accountNumber, owner = "Chuck Norris", currency = CurrencyUnit.EUR)
    }

    private val generateStatementUseCaseApi: GenerateStatementUseCaseApi = mock {  }
    private val depositUseCaseApi: DepositUseCaseApi = mock { }
    private val withdrawalUseCaseApi: WithdrawalUseCaseApi = mock {  }
    private val accountConsole = AccountConsole(
        depositUseCaseApi, withdrawalUseCaseApi, generateStatementUseCaseApi
    )

    @Test
    internal fun `print statement of an empty account should return ony headers`() {
        given(generateStatementUseCaseApi.getStatement(account)).willReturn(listOf())
        val statement = accountConsole.printStatement(account)
        assertThat(statement).isEqualTo("""
            transaction | date | amount | balance
            
        """.trimIndent())
    }

    @Test
    internal fun `print statement of an active account should return 4 lines`() {
        // GIVEN
        val dateTimeFirstTransaction = LocalDateTime.parse("2022-01-01T15:30")
        val dateTimeSecondTransaction = LocalDateTime.parse("2022-01-02T15:30")
        val dateTimeThirdTransaction = LocalDateTime.parse("2022-01-03T15:30")

        val statementLines = listOf(
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
        given(generateStatementUseCaseApi.getStatement(account)).willReturn(statementLines)
        val statement = accountConsole.printStatement(account)
        assertThat(statement).isEqualTo("""
            transaction | date | amount | balance
            DEPOSIT | $dateTimeFirstTransaction | EUR 500.00 | EUR 500.00
            WITHDRAWAL | $dateTimeSecondTransaction | EUR 300.00 | EUR 200.00
            DEPOSIT | $dateTimeThirdTransaction | EUR 150.00 | EUR 350.00
            
        """.trimIndent())
    }

}