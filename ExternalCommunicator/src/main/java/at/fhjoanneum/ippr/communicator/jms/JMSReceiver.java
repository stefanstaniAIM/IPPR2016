package at.fhjoanneum.ippr.communicator.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JMSReceiver {

  private final static Logger LOG = LoggerFactory.getLogger(JMSReceiver.class);

  @JmsListener(destination = "${jms.communicator.inbox}", containerFactory = "${jms.factory}",
      concurrency = "${jms.maxConcurrency}")
  public void receiveMessage(final String message) {
    LOG.debug("Received mesage [{}]", message);
  }
}
