package at.fhjoanneum.ippr.communicator.examples;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;

@Transactional
public abstract class AbstractExample implements CommandLineRunner {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractExample.class);

  protected abstract EntityManager getEntityManager();

  protected abstract void createData();

  protected abstract String getName();

  @Async
  @Override
  public void run(final String... args) throws Exception {
    createData();
    LOG.debug("Inserting of [{}] finished", getName());
  }
}
