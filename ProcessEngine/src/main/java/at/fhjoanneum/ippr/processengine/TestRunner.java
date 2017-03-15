package at.fhjoanneum.ippr.processengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObject;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;
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

    final BusinessObjectField fieldA = new BusinessObjectField("von", "STRING", "morgen");
    final BusinessObjectField fieldB = new BusinessObjectField("bis", "STRING", "unendlich");
    final BusinessObject boA = new BusinessObject("zeitraum", Sets.newHashSet(fieldA, fieldB));

    final BusinessObjectField fieldC = new BusinessObjectField("ort", "STRING", "New York");
    final BusinessObject boB = new BusinessObject("ziel", Sets.newHashSet(fieldC));

    externalCommunicatorClient.handleExternalOutputMessage(
        new ExternalOutputMessage("123-55", Sets.newHashSet(boA, boB)));
  }
}
