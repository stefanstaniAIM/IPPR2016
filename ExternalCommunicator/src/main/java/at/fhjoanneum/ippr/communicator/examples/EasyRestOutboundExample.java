package at.fhjoanneum.ippr.communicator.examples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.rest.RestConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypecomposer.DataTypeComposerBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.field.MessageProtocolFieldBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.BasicConfiguration;
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
    final RestConfigurationBuilder basicBuilder =
        (RestConfigurationBuilder) new RestConfigurationBuilder().name("easy rest outbound");

    basicBuilder.composerClass("at.fhjoanneum.ippr.communicator.composer.XmlComposer");

    final DataTypeComposer stringComposer = new DataTypeComposerBuilder().dataType(DataType.STRING)
        .composerClass("at.fhjoanneum.ippr.communicator.composer.datatype.StringComposer").build();
    basicBuilder.addParser(stringComposer);

    final MessageProtocol outboundProtocol =
        new MessageProtocolBuilder().internalName("zeitraum").externalName("timeframe").build();
    basicBuilder.messageProtocol(outboundProtocol);
    final MessageProtocolField fieldA = new MessageProtocolFieldBuilder().internalName("von")
        .externalName("from").dataType(DataType.STRING).messageProtocol(outboundProtocol).build();
    final MessageProtocolField fieldB = new MessageProtocolFieldBuilder().internalName("bis")
        .externalName("to").dataType(DataType.STRING).messageProtocol(outboundProtocol).build();

    final BasicConfiguration basicConfig = basicBuilder.build();

    entityManager.persist(stringComposer);
    entityManager.persist(outboundProtocol);
    entityManager.persist(fieldA);
    entityManager.persist(fieldB);
    entityManager.persist(basicConfig);
  }

  @Override
  protected String getName() {
    return "easy rest outbound example";
  }

}
