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
import org.mockito.kotlin.*
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import java.time.LocalDateTime
import java.util.*

class AccountWithdrawalTest {
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

    private val accountService: AccountServiceApi =
        AccountService(transactionRepositorySpi = transactionRepositorySpi, clock = TestClock())


    @Test
    internal fun `withdrawal some amount from account should create a withdrawal transaction`() {
        // GIVEN
        val amountToWithdrawal = Money.parse("EUR 100")
        given(transactionRepositorySpi.findTransactionByAccountNumber(accountNumber)).willReturn(
            listOf(Transaction(
                type = TransactionType.DEPOSIT, amount = Money.parse("EUR 500"),
                dateTime = mockTransactionDateTime,
                accountNumber = accountNumber
            ),
                Transaction(
                    type = TransactionType.WITHDRAWAL, amount = Money.parse("EUR 300"),
                    dateTime = mockTransactionDateTime,
                    accountNumber = accountNumber
                )
            ))
        // WHEN

        accountService.withdrawal(account = account, amount = amountToWithdrawal)

        // THEN

        val expectedTransactionToCreate = Transaction(type = TransactionType.WITHDRAWAL,
            amount = amountToWithdrawal,
            dateTime = mockTransactionDateTime,
            accountNumber = accountNumber)
        verify(transactionRepositorySpi, times(1)).createTransaction(expectedTransactionToCreate)
    }

    @Test
    internal fun `withdrawal all the money from account should create a withdrawal transaction`() {
        // GIVEN
        val amountToWithdrawal = Money.parse("EUR 100")
        given(transactionRepositorySpi.findTransactionByAccountNumber(accountNumber)).willReturn(
            listOf(Transaction(
                type = TransactionType.DEPOSIT, amount = Money.parse("EUR 100"),
                dateTime = mockTransactionDateTime,
                accountNumber = accountNumber
            )
            ))
        // WHEN

        accountService.withdrawal(account = account, amount = amountToWithdrawal)

        // THEN

        val expectedTransactionToCreate = Transaction(type = TransactionType.WITHDRAWAL,
            amount = amountToWithdrawal,
            dateTime = mockTransactionDateTime,
            accountNumber = accountNumber)
        verify(transactionRepositorySpi, times(1)).createTransaction(expectedTransactionToCreate)
    }

    @Test
    internal fun `withdrawal an amount of money greater than the balance should throw an exception`() {
        // GIVEN
        val amountToWithdrawal = Money.parse("EUR 1000")
        given(transactionRepositorySpi.findTransactionByAccountNumber(accountNumber)).willReturn(
            listOf(Transaction(
                type = TransactionType.DEPOSIT, amount = Money.parse("EUR 500"),
                dateTime = mockTransactionDateTime,
                accountNumber = accountNumber
            )))
        // WHEN // THEN
        assertThrows<UnsupportedOperationException> {
            accountService.withdrawal(account = account,
                amount = amountToWithdrawal)
        }
        verify(transactionRepositorySpi, times(1)).findTransactionByAccountNumber(accountNumber)
        verifyNoMoreInteractions(transactionRepositorySpi)
    }

    @Test
    internal fun `withdrawal zero amount of money should throw an exception`() {
        // GIVEN
        val amountToWithdrawal = Money.zero(CurrencyUnit.EUR)
        // WHEN // THEN
        assertThrows<IllegalArgumentException> {
            accountService.withdrawal(account = account, amount = amountToWithdrawal)
        }
        verifyNoMoreInteractions(transactionRepositorySpi)
    }


    @Test
    internal fun `withdraw an amount in different account's currency should should throw an exception`() {
        // GIVEN
        val amountToDeposit = Money.parse("USD 100")
        // WHEN // THEN
        assertThrows<IllegalArgumentException> {
            accountService.withdrawal(account = account, amount = amountToDeposit)
        }
        verifyNoMoreInteractions(transactionRepositorySpi)
    }

    @Test
    internal fun `withdrawal a negative amount of money should throw an exception`() {
        // GIVEN
        val amountToDeposit = Money.parse("EUR 1000").negated()
        // WHEN // THEN

        assertThrows<IllegalArgumentException> {
            accountService.withdrawal(account = account,
                amount = amountToDeposit)
        }
        verifyNoMoreInteractions(transactionRepositorySpi)
    }
}