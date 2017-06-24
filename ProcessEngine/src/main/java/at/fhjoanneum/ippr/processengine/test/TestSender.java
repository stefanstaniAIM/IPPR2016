package at.fhjoanneum.ippr.processengine.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import at.fhjoanneum.ippr.processengine.services.EventLoggerSender;

// @Component
public class TestSender implements CommandLineRunner {

  @Autowired
  private EventLoggerSender sender;

  @Override
  public void run(final String... args) throws Exception {
    final String result = sender.send();
    System.out.println(result);
  }

}
