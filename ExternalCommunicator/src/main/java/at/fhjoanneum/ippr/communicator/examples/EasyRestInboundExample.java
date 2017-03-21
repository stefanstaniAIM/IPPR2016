package at.fhjoanneum.ippr.communicator.examples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.communicator.persistence.entities.basic.inbound.rest.RestInboundConfigurationBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.datatypeparser.DataTypeParserBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.MessageProtocolBuilder;
import at.fhjoanneum.ippr.communicator.persistence.entities.protocol.field.MessageProtocolFieldBuilder;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.basic.inbound.BasicInboundConfiguration;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocolField;

@Component
public class EasyRestInboundExample extends AbstractExample {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  protected void createData() {
    final RestInboundConfigurationBuilder basic = new RestInboundConfigurationBuilder();
    basic.endpoint("wuhu").name("json rest")
        .parserClass("at.fhjoanneum.ippr.communicator.parser.JsonParser");

    final DataTypeParser stringParser = new DataTypeParserBuilder().dataType(DataType.STRING)
        .parserClass("at.fhjoanneum.ippr.communicator.parser.datatype.StringParser")
        .description("blaaa").build();

    basic.addParser(stringParser);

    final MessageProtocol inboundProtocol = new MessageProtocolBuilder()
        .internalName("Hotel Reservation").externalName("Hotel Reservierung").build();
    basic.messageProtocol(inboundProtocol);
    final MessageProtocolField fieldA = new MessageProtocolFieldBuilder().internalName("Name")
        .externalName("Name").dataType(DataType.STRING).messageProtocol(inboundProtocol).build();
    final MessageProtocolField fieldB = new MessageProtocolFieldBuilder().internalName("Stars")
        .externalName("Sterne").dataType(DataType.STRING).messageProtocol(inboundProtocol).build();

    final BasicInboundConfiguration basicConfig = basic.build();

    entityManager.persist(stringParser);
    entityManager.persist(inboundProtocol);
    entityManager.persist(fieldA);
    entityManager.persist(fieldB);
    entityManager.persist(basicConfig);
  }

  @Override
  protected String getName() {
    return "easy rest inbound example";
  }

}
