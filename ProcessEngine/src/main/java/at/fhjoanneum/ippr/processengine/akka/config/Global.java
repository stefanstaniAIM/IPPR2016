package at.fhjoanneum.ippr.processengine.akka.config;

import java.util.concurrent.TimeUnit;

import akka.util.Timeout;
import scala.concurrent.duration.Duration;

public class Global {

  public Global() {}

  public final static Timeout TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));
}
