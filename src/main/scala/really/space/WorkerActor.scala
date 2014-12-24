package really.space

import _root_.akka.actor.Actor
import _root_.akka.actor.ActorLogging
import _root_.akka.actor.Stash
import really.space.Worker._

/**
 * Created by halsum on 12/23/14.
 */
class WorkerActor extends Actor with ActorLogging with Stash {

  def receive: Receive = starterReceive

  def starterReceive: Receive = {
    case Ping =>
      println(Ping)
      sender() ! Pong
    case StartGreet =>
      println(StartGreet)
      context.become(workerReceive)
      unstashAll()
    case x =>
      log.error("Message will be stashed: " + x)
      stash()
  }

  def workerReceive: Receive = {
    case Greet1 =>
      println(Greet1)
      sender() ! Greet2
    case Greet2 =>
      println(Greet2)
      sender() ! Greet1
    case Done =>
      context.become(starterReceive)
      unstashAll()
    case x =>
      log.error("Message will be stashed: " + x)
      stash()
  }
}


object Worker {

  case object Ping

  case object Pong

  case object StartGreet

  case object Greet1

  case object Greet2

  case object Done

}