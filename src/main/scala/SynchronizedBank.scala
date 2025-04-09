class SynchronizedBank extends Bank {
  // def transfer(fromId: Int, toId: Int, amount: Int): Unit = synchronized { // Alternative fix to deadlock ?
  def transfer(fromId: Int, toId: Int, amount: Int): Unit = {
    val fromAccount = getAccount(fromId)
    val toAccount = getAccount(toId)

    if (fromAccount.balance < amount)
      throw new RuntimeException("Insufficient funds in account.")

    // val accounts = List(fromAccount, toAccount).sortBy(_.id) // Fixing the deadlock

    // accounts.head.synchronized { // Fixing the deadlock
    fromAccount.synchronized {
      // Thread.sleep(50) // This line enables a deadlock
      // accounts.last.synchronized { // Fixing the deadlock
      toAccount.synchronized {
        fromAccount.balance -= amount
        toAccount.balance += amount
      }
    }
  }
}
