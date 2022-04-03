import io.shodo.domain.AccountService
import io.shodo.domain.AccountServiceApi
import io.shodo.domain.Clock
import io.shodo.domain.TransactionRepositorySpi
import io.shodo.domain.model.Account
import io.shodo.domain.model.Transaction
import io.shodo.domain.model.TransactionType
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.time.LocalDateTime
import java.util.*

class AccountDepositTest {

    private val transactionRepositorySpi: TransactionRepositorySpi = mock { }

    companion object{
        private val mockTransactionDateTime = LocalDateTime.parse("2022-01-01T15:30")
        private val accountNumber = UUID.randomUUID().toString()
        private val account = Account(number = accountNumber, owner = "Chuck Norris", currency = CurrencyUnit.EUR)
    }

    class TestClock: Clock() {
        override fun getLocalDateTime(): LocalDateTime {
            return mockTransactionDateTime
        }
    }
    private val accountService: AccountServiceApi = AccountService(transactionRepositorySpi = transactionRepositorySpi, clock = TestClock())


    @Test
    internal fun `deposit money to account should create deposit transaction`() {
        // GIVEN
        val amountToDeposit = Money.parse("EUR 1000")
        // WHEN

        accountService.deposit(account = account, amount = amountToDeposit)

        // THEN

        val expectedTransactionToCreate = Transaction(type = TransactionType.DEPOSIT,
            amount = amountToDeposit,
            dateTime = mockTransactionDateTime,
            accountNumber = accountNumber)
        verify(transactionRepositorySpi, times(1)).createTransaction(expectedTransactionToCreate)
    }

    @Test
    internal fun `deposit negative amount of money should throw an exception`() {
        // GIVEN
        val amountToDeposit = Money.parse("EUR 1000").negated()
        // WHEN // THEN
        assertThrows<IllegalArgumentException> { accountService.deposit(account = account, amount = amountToDeposit) }
        verifyNoMoreInteractions(transactionRepositorySpi)
    }

    @Test
    internal fun `deposit zero amount of money should throw an exception`() {
        // GIVEN
        val amountToDeposit = Money.zero(CurrencyUnit.EUR)
        // WHEN // THEN
        assertThrows<IllegalArgumentException> { accountService.deposit(account = account, amount = amountToDeposit) }
        verifyNoMoreInteractions(transactionRepositorySpi)
    }

    @Test
    internal fun `deposit an amount in different account's currency should should throw an exception`() {
        // GIVEN
        val amountToDeposit = Money.parse("USD 100")
        // WHEN // THEN
        assertThrows<IllegalArgumentException> { accountService.deposit(account = account, amount = amountToDeposit) }
        verifyNoMoreInteractions(transactionRepositorySpi)
    }
}