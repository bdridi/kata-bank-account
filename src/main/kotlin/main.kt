import io.shodo.application.AccountConsole
import io.shodo.domain.AccountService
import io.shodo.domain.Clock
import io.shodo.domain.model.Account
import io.shodo.infrastructure.InMemoryTransactionRepository
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.util.*

fun main(args: Array<String>) {
    // Config
    val transactionRepository = InMemoryTransactionRepository()
    val accountService = AccountService(transactionRepository, Clock())
    val accountConsole = AccountConsole(accountService)

    // create an account
    val account = Account(number = UUID.randomUUID().toString(), owner = "Chuck Norris", currency = CurrencyUnit.EUR)

    // create transactions

    accountConsole.deposit(account, Money.parse("EUR 100"))
    accountConsole.deposit(account, Money.parse("EUR 300"))
    accountConsole.withdrawal(account, Money.parse("EUR 100"))
    accountConsole.deposit(account, Money.parse("EUR 20"))
    accountConsole.withdrawal(account, Money.parse("EUR 40"))
    accountConsole.withdrawal(account, Money.parse("EUR 45"))

    // print statement

    println(accountConsole.printStatement(account))
}