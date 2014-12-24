package really.space

import akka.actor.{Props, ActorSystem}

/**
 * Created by halsum on 12/23/14.
 */
object Starter {
  def main(args: Array[String]) {
    val system = ActorSystem("Main")
    val ac = system.actorOf(Props[WorkerActor])
    println("Server started")
    ac ! Worker.Greet1
    ac ! Worker.Ping
  }
}

