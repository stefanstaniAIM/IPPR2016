package at.fhjoanneum.ippr.processengine.akka.actors.process;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import akka.actor.UntypedActor;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldInstanceDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectInstanceDTO;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectChangeMessage;
import at.fhjoanneum.ippr.processengine.parser.DbValueParser;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.StateRepository;

@Transactional
@Component("BusinessObjectCheckActor")
@Scope("prototype")
public class BusinessObjectCheckActor extends UntypedActor {

  private final static Logger LOG = LoggerFactory.getLogger(BusinessObjectCheckActor.class);

  @Autowired
  private StateRepository stateRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private DbValueParser parser;

  private final Long currentState;

  public BusinessObjectCheckActor(final Long currentState) {
    this.currentState = currentState;
  }

  @Override
  public void onReceive(final Object obj) throws Throwable {
    if (obj instanceof StateObjectChangeMessage.Request) {
      handleStateObjectChangeMessage(obj);
    } else {
      unhandled(obj);
    }
  }

  private void handleStateObjectChangeMessage(final Object obj) {
    final StateObjectChangeMessage.Request request = (StateObjectChangeMessage.Request) obj;

    boolean correct = true;

    final State state = stateRepository.findOne(currentState);

    final List<BusinessObjectFieldModel> fieldModels = state.getBusinessObjectModels().stream()
        .map(BusinessObjectModel::getBusinessObjectFieldModels).flatMap(List::stream)
        .collect(Collectors.toList());

    final Map<Long, String> fieldInstances =
        convertToMap(request.getStateObjectChangeDTO().getBusinessObjects());

    for (final BusinessObjectFieldModel fieldModel : fieldModels) {
      correct = checkField(fieldModel, fieldInstances.get(fieldModel.getBofmId()));
      if (!correct) {
        LOG.info("Result of business object check was false");
        getSender().tell(Boolean.FALSE, getSelf());
        getContext().stop(getSelf());
        return;
      }
    }

    LOG.info("Result of business object check was true");
    getSender().tell(Boolean.TRUE, getSelf());
    getContext().stop(getSelf());
  }

  private Map<Long, String> convertToMap(final List<BusinessObjectInstanceDTO> businessObjects) {
    if (businessObjects == null || businessObjects.isEmpty()) {
      return Maps.newHashMap();
    }

    return businessObjects.stream().map(BusinessObjectInstanceDTO::getFields).flatMap(List::stream)
        .collect(Collectors.toMap(BusinessObjectFieldInstanceDTO::getBofmId,
            BusinessObjectFieldInstanceDTO::getValue));
  }

  private boolean checkField(final BusinessObjectFieldModel fieldModel, final String fieldValue) {
    final BusinessObjectFieldPermission fieldPermission = businessObjectFieldPermissionRepository
        .getBusinessObjectFieldPermissionInState(fieldModel.getBofmId(), currentState);

    if (fieldPermission == null) {
      LOG.warn("Could not find field permission in state S_ID [{}] for field model BOFM_ID [{}]",
          currentState, fieldModel.getBofmId());
      return true;
    }

    if (fieldPermission.getPermission().equals(FieldPermission.READ_WRITE)) {
      if (fieldPermission.isMandatory() && StringUtils.isBlank(fieldValue)) {
        LOG.error("Field model BOFM_ID [{}] is mandatory but is empty", fieldModel.getBofmId());
        return false;
      } else if (!fieldPermission.isMandatory() && StringUtils.isBlank(fieldValue)) {
        return true;
      }

      final boolean canParse = parser.canParse(fieldValue, fieldModel.getFieldType());
      if (!canParse) {
        LOG.error("Could not parse value [{}] to type [{}]", fieldValue, fieldModel.getFieldType());
      }
      return canParse;
    }
    return true;
  }
}
