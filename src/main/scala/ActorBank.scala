package com.springer.nemo
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem, Scheduler}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.util.Timeout
import org.apache.pekko.actor.typed.scaladsl.AskPattern._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

class ActorBank extends Bank {
  import ActorBank._

  def transfer(fromId: Int, toId: Int, amount: Int): Unit = {
    val fromAccount = getAccount(fromId)
    val toAccount = getAccount(toId)

    val response: Future[ActorProtocol] = actorSystem.ask(ref => Transfer(fromAccount, toAccount, amount, ref))
    Await.result(response, timeout.duration)
  }
}

private object ActorBank {
  private val actorSystem = ActorSystem[ActorProtocol](BankActor(), "BankActorSystem")
  private implicit val timeout: Timeout = new Timeout(2.seconds)
  private implicit val scheduler: Scheduler = actorSystem.scheduler

  private sealed trait ActorProtocol
  private case class Transfer(fromAccount: BankAccount, toAccount: BankAccount, amount: Int, replyTo: ActorRef[ActorProtocol]) extends ActorProtocol
  private case object Done extends ActorProtocol

  private object BankActor {
    def apply(): Behaviors.Receive[ActorProtocol] = Behaviors.receive((_, msg) =>
      msg match {
        case Transfer(fromAccount, toAccount, amount, replyTo) =>
          if (fromAccount.balance < amount)
            throw new RuntimeException("Insufficient funds in account.")

          fromAccount.balance -= amount
          toAccount.balance += amount

          replyTo.tell(Done)

          Behaviors.same

        case _ => Behaviors.same
      }
    )
  }
}
