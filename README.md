# Kata bank account 

Bank account kata simulate a simple personal bank account experience : 
· Deposit and Withdrawal
· Account statement (date, amount, balance)
· Statement printing

## Built with

- Kotlin
- Maven

## How to launch

- Create an account and some transactions in file `main.kt` and the print statement as the example below :


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

Now run the main class `src/main/kotlin/main.kt`
