package com.springer.nemo

abstract class Bank {
  private val accounts: Seq[BankAccount] = 1.to(25).map(id => new BankAccount(id, 500))

  def transfer(fromId: Int, toId: Int, amount: Int): Unit

  protected def getAccount(accountId: Int): BankAccount =
    accounts.find(_.id == accountId).getOrElse(throw new RuntimeException("Account not found"))

  def balance(accountId: Int): Int = getAccount(accountId).balance
}
