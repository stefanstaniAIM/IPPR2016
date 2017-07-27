package at.fhjoanneum.ippr.eventlogger.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
@Component
public class TestRunner implements CommandLineRunner {

  @Override
  public void run(final String... args) throws Exception {

  }

}
