package at.fhjoanneum.ippr.communicator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObject;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalOutputMessage;
import at.fhjoanneum.ippr.communicator.services.ExternalCommunicatorService;

@Component
@Transactional
public class TestRunner implements CommandLineRunner {

  @Autowired
  private ExternalCommunicatorService external;

  @Override
  public void run(final String... args) throws Exception {
    final BusinessObjectField fieldA = new BusinessObjectField("von", "STRING", "morgen");
    final BusinessObjectField fieldB = new BusinessObjectField("bis", "STRING", "unendlich");
    final BusinessObject boA = new BusinessObject("zeitraum", Sets.newHashSet(fieldA, fieldB));

    final BusinessObjectField fieldC = new BusinessObjectField("ort", "STRING", "New York");
    final BusinessObject boB = new BusinessObject("ziel", Sets.newHashSet(fieldC));

    external.handleExternalOutputMessage(
        new ExternalOutputMessage("123-55", Sets.newHashSet(boA, boB)));
    // composeSupervisorActor.tell(new ComposeMessageCreateCommand("xoxo"), ActorRef.noSender());
  }
}
