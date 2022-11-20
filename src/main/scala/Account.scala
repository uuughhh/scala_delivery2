import exceptions._

class Account(val bank: Bank, initialBalance: Double) {

  class Balance(var amount: Double) {}

  val balance = new Balance(initialBalance)

  // TODO
  // for project task 1.2: implement functions
  // for project task 1.3: change return type and update function bodies
  def withdraw(amount: Double): Either[Unit, String] = {
    balance.synchronized {
      if (amount < 0) return Right("Cannot withdraw negative amount")
      if (amount > balance.amount)
        return Right("Cannot withdraw more than amount")
      balance.amount -= amount
    }
    Left(())
  }
  def deposit(amount: Double): Either[Unit, String] = {
    balance.synchronized {
      if (amount < 0) return Right("Cannot deposit a negative amount")
      balance.amount += amount
    }
    Left(())
  }
  def getBalanceAmount: Double = {
    balance.amount
  }

  def transferTo(account: Account, amount: Double) = {
    bank addTransactionToQueue (this, account, amount)
  }

}
