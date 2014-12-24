
import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, TestProbe, TestKit, TestActorRef}
import com.typesafe.config.ConfigFactory
import really.space.Worker._
import really.space.WorkerActor
import org.scalatest._

/**
 * Created by halsum on 12/23/14.
 */
class WorkerActorSpec extends
TestKit(ActorSystem("test", ConfigFactory.parseString( """akka.loggers = ["akka.testkit.TestEventListener"]""")))
with FlatSpecLike with Matchers {

  "Worker Actor" should "responde to Ping and replay with Pong " in {
    val probe = TestProbe()
    val worker = TestActorRef[WorkerActor]
    worker.tell(Ping, probe.ref)
    probe.expectMsg(Pong)

  }

  it should "stash any message other than Ping or StartGreet " in {
    val probe = TestProbe()
    val worker = TestActorRef[WorkerActor]
    EventFilter.error(occurrences = 1, message = s"Message will be stashed: Greet1") intercept {
      worker.tell(Greet1, probe.ref)
    }
    EventFilter.error(occurrences = 1, message = s"Message will be stashed: Greet2") intercept {
      worker.tell(Greet2, probe.ref)
    }
  }

  it should "unstash when message is startGreeting " in {
    val probe = TestProbe()
    val worker = TestActorRef[WorkerActor]
    worker.tell(StartGreet, probe.ref)
    worker.tell(Greet1, probe.ref)
    probe.expectMsg(Greet2)
    worker.tell(Greet2, probe.ref)
    probe.expectMsg(Greet1)
  }

  it should "proccess stashed messages after unstash" in {
    val probe = TestProbe()
    val worker = TestActorRef[WorkerActor]
    worker.tell(Greet1, probe.ref)
    worker.tell(Greet2, probe.ref)
    worker.tell(StartGreet, probe.ref)
    probe.expectMsg(Greet2)
    probe.expectMsg(Greet1)

  }
}
