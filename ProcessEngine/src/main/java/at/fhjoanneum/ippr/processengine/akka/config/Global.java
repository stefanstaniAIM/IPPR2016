package at.fhjoanneum.ippr.processengine.akka.config;

import java.util.concurrent.TimeUnit;

import akka.util.Timeout;
import scala.concurrent.duration.Duration;

public class Global {

  public Global() {}

  public final static Timeout TIMEOUT = new Timeout(Duration.create(60, TimeUnit.SECONDS));

  public final static Long DESTROY_ID = -1L;
}
