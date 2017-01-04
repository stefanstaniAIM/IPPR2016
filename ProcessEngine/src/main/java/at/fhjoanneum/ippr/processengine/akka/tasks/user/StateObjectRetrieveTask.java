package at.fhjoanneum.ippr.processengine.akka.tasks.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.commons.dto.processengine.StateDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.BusinessObjectFieldDTO;
import at.fhjoanneum.ippr.commons.dto.processengine.stateobject.StateObjectDTO;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.state.SubjectState;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.processengine.akka.messages.process.workflow.StateObjectMessage;
import at.fhjoanneum.ippr.processengine.akka.tasks.AbstractTask;
import at.fhjoanneum.ippr.processengine.composer.DbValueComposer;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldPermissionRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Component("User.StateObjectRetrieveTask")
@Scope("prototype")
public class StateObjectRetrieveTask extends AbstractTask<StateObjectMessage.Request> {

  private final static Logger LOG = LoggerFactory.getLogger(StateObjectRetrieveTask.class);

  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private BusinessObjectFieldPermissionRepository businessObjectFieldPermissionRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;
  @Autowired
  private DbValueComposer valueComposer;

  @Override
  public boolean canHandle(final Object obj) {
    return obj instanceof StateObjectMessage.Request;
  }

  @Override
  public void execute(final StateObjectMessage.Request request) throws Exception {
    final SubjectState subjectState =
        Optional
            .ofNullable(subjectStateRepository
                .getSubjectStateOfUser(request.getPiId(), request.getUserId()))
            .get();

    final List<BusinessObjectDTO> businessObjects = Lists.newArrayList();

    for (final BusinessObjectModel businessObjectModel : subjectState.getCurrentState()
        .getBusinessObjectModels()) {

      LOG.debug("Found business object model: {}", businessObjectModel);

      final List<BusinessObjectFieldDTO> fields = Lists.newArrayList();
      final Optional<BusinessObjectInstance> businessObjectInstanceOpt = Optional
          .ofNullable(businessObjectInstanceRepository.getBusinessObjectInstanceOfModelInProcess(
              request.getPiId(), businessObjectModel.getBomId()));

      if (!businessObjectInstanceOpt.isPresent()) {
        LOG.debug("No business object instance is present for BOM_ID [{}] in process PI_ID [{}]",
            businessObjectModel.getBomId(), request.getPiId());
      }

      for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectModel
          .getBusinessObjectFieldModels()) {
        final BusinessObjectFieldPermission businessObjectFieldPermission =
            businessObjectFieldPermissionRepository.getBusinessObjectFieldPermissionInState(
                businessObjectFieldModel.getBofmId(), subjectState.getCurrentState().getSId());

        if (!businessObjectFieldPermission.getPermission().equals(FieldPermission.NONE)) {
          final Long bofmId = businessObjectFieldModel.getBofmId();
          final String name = businessObjectFieldModel.getFieldName();
          final String type = businessObjectFieldModel.getFieldType().name();
          final boolean required = businessObjectFieldPermission.isMandatory();
          final boolean readOnly =
              businessObjectFieldPermission.getPermission().equals(FieldPermission.READ) ? true
                  : false;

          String value = null;
          Long bofiId = null;
          if (businessObjectInstanceOpt.isPresent()) {
            final Optional<BusinessObjectFieldInstance> fieldInstanceOpt = businessObjectInstanceOpt
                .get().getBusinessObjectFieldInstanceOfFieldModel(businessObjectFieldModel);

            if (fieldInstanceOpt.isPresent()) {
              final BusinessObjectFieldInstance fieldInstance = fieldInstanceOpt.get();
              bofiId = fieldInstance.getBofiId();

              value = valueComposer.compose(fieldInstance.getValue(),
                  fieldInstance.getBusinessObjectFieldModel().getFieldType());
            }
          }
          fields.add(
              new BusinessObjectFieldDTO(bofmId, bofiId, name, type, required, readOnly, value));
        } else {
          LOG.debug("Not necessary to add field [{}] since permission is 'NONE'");
          continue;
        }
      }
      businessObjects.add(new BusinessObjectDTO(businessObjectModel.getBomId(),
          businessObjectInstanceOpt.isPresent() ? businessObjectInstanceOpt.get().getBoiId() : null,
          businessObjectModel.getName(), fields));
    }

    final List<StateDTO> nextStates = subjectState.getCurrentState().getToStates().stream()
        .map(state -> new StateDTO(state.getToState().getSId(), state.getToState().getName()))
        .collect(Collectors.toList());

    getSender().tell(new StateObjectMessage.Response(
        new StateObjectDTO(request.getPiId(), subjectState.getSsId(), businessObjects, nextStates)),
        getSelf());
  }

}
