package at.fhjoanneum.ippr.communicator.examples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.communicator.global.GlobalKey;
import at.fhjoanneum.ippr.communicator.persistence.entities.basic.outbound.BasicOutboundConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.config.ConfigurationAssignement;
import at.fhjoanneum.ippr.communicator.persistence.entities.config.ConfigurationAssignmentBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.field.MessageProtocolFieldBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.outbound.BasicOutboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypecomposer.DataTypeComposer;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

@Component
public class EasyRestOutboundExample extends AbstractExample {

  private final static Logger LOG = LoggerFactory.getLogger(EasyRestOutboundExample.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  protected void createData() {
    final BasicOutboundConfigurationBuilder basicBuilder =
        new BasicOutboundConfigurationBuilder().name("easy rest outbound");

    basicBuilder.composerClass("at.fhjoanneum.ippr.communicator.composer.XmlComposer");
    basicBuilder.sendPlugin("at.fhjoanneum.ippr.communicator.plugins.send.XmlSendPlugin");
    basicBuilder.addConfigurationEntry(GlobalKey.ENDPOINT, "http://localhost:10000/ec/test");
    basicBuilder.addConfigurationEntry(GlobalKey.TYPE, "TYPE");
    basicBuilder.addConfigurationEntry(GlobalKey.TRANSFER_ID, "TRANSFER-ID");

    final DataTypeComposer stringComposer = new DataTypeComposerBuilder().dataType(DataType.STRING)
        .composerClass("at.fhjoanneum.ippr.communicator.composer.datatype.StringComposer").build();
    basicBuilder.addComposer(stringComposer);

    final MessageProtocol outboundProtocol = new MessageProtocolBuilder()
        .internalName("Reiseantrag").externalName("TravelRequest").build();
    basicBuilder.messageProtocol(outboundProtocol);
    final MessageProtocolField fieldA = new MessageProtocolFieldBuilder().internalName("Von")
        .externalName("From").dataType(DataType.STRING).messageProtocol(outboundProtocol).build();
    final MessageProtocolField fieldB = new MessageProtocolFieldBuilder().internalName("Bis")
        .externalName("To").dataType(DataType.STRING).messageProtocol(outboundProtocol).build();
    final MessageProtocolField fieldC =
        new MessageProtocolFieldBuilder().internalName("Ort").externalName("Location")
            .dataType(DataType.STRING).messageProtocol(outboundProtocol).build();

    final BasicOutboundConfiguration basicConfig = basicBuilder.build();

    final ConfigurationAssignement map = new ConfigurationAssignmentBuilder().messageFlowId(7L)
        .outboundConfiguration(basicConfig).build();

    entityManager.persist(stringComposer);
    entityManager.persist(outboundProtocol);
    entityManager.persist(fieldA);
    entityManager.persist(fieldB);
    entityManager.persist(fieldC);
    entityManager.persist(basicConfig);
    entityManager.persist(map);
  }

  @Override
  protected String getName() {
    return "easy rest outbound example";
  }

}
