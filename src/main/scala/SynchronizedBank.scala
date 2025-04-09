package com.springer.nemo
import scala.util.Random

class SynchronizedBank extends Bank {
  def transfer(fromId: Int, toId: Int, amount: Int): Unit = {
    val fromAccount = getAccount(fromId)
    val toAccount = getAccount(toId)

    if (fromAccount.balance < amount)
      throw new RuntimeException("Insufficient funds in account.")

    fromAccount.synchronized {
      // This line enables a deadlock
      // Thread.sleep(50)
      toAccount.synchronized {
        fromAccount.balance -= amount
        toAccount.balance += amount
      }
    }
  }

}
