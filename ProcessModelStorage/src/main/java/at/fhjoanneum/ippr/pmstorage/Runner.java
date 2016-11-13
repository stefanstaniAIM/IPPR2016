package at.fhjoanneum.ippr.pmstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.pmstorage.repositories.ProcessModelController;

@Component
class Runner implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ProcessModelController controller;

  @Override
  public void run(final String... args) throws Exception {

    // controller.create();
  }
}
