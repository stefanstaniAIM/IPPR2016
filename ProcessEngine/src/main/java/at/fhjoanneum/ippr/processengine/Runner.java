package at.fhjoanneum.ippr.processengine;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import akka.util.Timeout;
import at.fhjoanneum.ippr.processengine.repositories.ProcessModelController;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

@Component
class Runner implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ActorSystem actorSystem;

	@Autowired
	private SpringExtension springExtension;

	@Autowired
	private ProcessModelController controller;

	@Override
	public void run(final String... args) throws Exception {
		try {
			final ActorRef workerActor = actorSystem.actorOf(springExtension.props("workerActor"), "worker-actor");

			workerActor.tell(new WorkerActor.Request(), null);
			workerActor.tell(new WorkerActor.Request(), null);
			workerActor.tell(new WorkerActor.Request(), null);

			final FiniteDuration duration = FiniteDuration.create(1, TimeUnit.SECONDS);
			final Future<Object> awaitable = Patterns.ask(workerActor, new WorkerActor.Response(),
					Timeout.durationToTimeout(duration));

			logger.info("Response: " + Await.result(awaitable, duration));

			// controller.create();
		} finally {
			actorSystem.terminate();
			Await.result(actorSystem.whenTerminated(), Duration.Inf());
		}
	}
}
