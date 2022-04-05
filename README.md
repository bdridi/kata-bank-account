# Kata bank account 

Bank account kata simulate a simple personal bank account experience : 
· Deposit and Withdrawal
· Account statement (date, amount, balance)
· Statement printing

## Built with

- Kotlin
- Maven

## How to launch

- Create an account, and some transactions in file `main.kt` and the print statement as the example below :


````kotlin
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

````

- Now run the main class `src/main/kotlin/main.kt`. The statement should be printed as the example below : 

```
transaction | date | amount | balance
DEPOSIT | 2022-04-05T21:35:47.909155 | EUR 100.00 | EUR 100.00
DEPOSIT | 2022-04-05T21:35:47.913154 | EUR 300.00 | EUR 400.00
WITHDRAWAL | 2022-04-05T21:35:47.913366 | EUR 100.00 | EUR 300.00
DEPOSIT | 2022-04-05T21:35:47.913418 | EUR 20.00 | EUR 320.00
WITHDRAWAL | 2022-04-05T21:35:47.913536 | EUR 40.00 | EUR 280.00
WITHDRAWAL | 2022-04-05T21:35:47.913621 | EUR 45.00 | EUR 235.00

```
