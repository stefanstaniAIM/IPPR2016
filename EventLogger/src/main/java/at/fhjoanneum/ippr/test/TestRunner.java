package at.fhjoanneum.ippr.test;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.persistence.EventLog;
import at.fhjoanneum.ippr.persistence.EventLogRepository;

@Transactional
@Component
public class TestRunner implements CommandLineRunner {

  @Autowired
  private EventLogRepository eventLogRepository;

  @Override
  public void run(final String... args) throws Exception {
    final EventLog eventLog = new EventLog(55L);
    eventLogRepository.save(eventLog);
  }

}
