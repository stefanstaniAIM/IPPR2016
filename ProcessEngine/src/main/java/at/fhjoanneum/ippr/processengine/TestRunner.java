package at.fhjoanneum.ippr.processengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.processengine.feign.ExternalCommunicatorClient;

@Component
public class TestRunner implements CommandLineRunner {

  private final static Logger LOG = LoggerFactory.getLogger(TestRunner.class);

  @Autowired
  private ExternalCommunicatorClient externalCommunicatorClient;

  @Override
  public void run(final String... args) throws Exception {
    LOG.debug(
        "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    // externalCommunicatorClient.sendReceiveSubmission(new ReceiveSubmissionDTO("5555"));
  }
}
