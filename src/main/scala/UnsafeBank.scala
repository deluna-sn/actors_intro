package com.springer.nemo

class UnsafeBank extends Bank {
  def transfer(fromId: Int, toId: Int, amount: Int): Unit = {
    val fromAccount = getAccount(fromId)
    val toAccount = getAccount(toId)

    if (fromAccount.balance < amount)
      throw new RuntimeException("Insufficient funds in account.")

    fromAccount.balance -= amount
    toAccount.balance += amount
  }
}
