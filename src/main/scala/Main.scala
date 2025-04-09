import java.util.concurrent.Executors
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

object Main {
  def main(args: Array[String]): Unit = {
    val threadPool = Executors.newFixedThreadPool(8)
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(threadPool)

    val bank = args.headOption match {
      case Some("unsafe") => new UnsafeBank()
      case Some("sync")   => new SynchronizedBank()
      case Some("actor")  => new ActorBank()
      case _              => throw new IllegalStateException()
    }

    val transfers = Future
      .sequence(
        List(
          Future(bank.transfer(1, 2, 200)),
          Future(bank.transfer(2, 1, 200)),
          Future(bank.transfer(1, 2, 200)),
          Future(bank.transfer(2, 1, 200)),
          Future(bank.transfer(3, 4, 200)),
          Future(bank.transfer(4, 3, 200)),
          Future(bank.transfer(3, 4, 200)),
          Future(bank.transfer(4, 3, 200))
        )
      )
      .map(_ =>
        1.to(4).foreach { id =>
          val balance = bank.balance(id)
          if (balance == 500)
            println(s"Balance for account $id is: ${bank.balance(id)}")
          else
            println(s"Balance for account $id is: ${bank.balance(id)} <------------")
        }
      )

    Await.result(transfers, 10.seconds)
  }
}
