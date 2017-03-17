package at.fhjoanneum.ippr.communicator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObject;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField;
import at.fhjoanneum.ippr.communicator.services.ExternalCommunicatorService;

@Component
@Transactional
public class TestRunner implements CommandLineRunner {

  @Autowired
  private ExternalCommunicatorService external;

  @Override
  public void run(final String... args) throws Exception {
    final BusinessObjectField fieldA = new BusinessObjectField("From", "STRING", "morgen");
    final BusinessObjectField fieldB = new BusinessObjectField("To", "STRING", "unendlich");
    final BusinessObject boA =
        new BusinessObject("Vacation Request Form", Sets.newHashSet(fieldA, fieldB));

    final BusinessObjectField fieldC = new BusinessObjectField("ort", "STRING", "New York");
    final BusinessObject boB = new BusinessObject("ziel", Sets.newHashSet(fieldC));

    // external.handleExternalOutputMessage(
    // new ExternalOutputMessage("123-55-7", Sets.newHashSet(boA, boB)));
  }
}
