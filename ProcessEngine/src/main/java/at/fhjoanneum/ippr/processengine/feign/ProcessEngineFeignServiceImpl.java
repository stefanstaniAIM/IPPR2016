package at.fhjoanneum.ippr.processengine.feign;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObject;
import at.fhjoanneum.ippr.commons.dto.communicator.BusinessObjectField;
import at.fhjoanneum.ippr.commons.dto.communicator.ExternalCommunicatorMessage;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.BusinessObjectInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceBuilder;
import at.fhjoanneum.ippr.persistence.entities.engine.businessobject.field.BusinessObjectFieldInstanceImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.state.SubjectStateImpl;
import at.fhjoanneum.ippr.persistence.entities.engine.subject.SubjectImpl;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectFieldInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.businessobject.BusinessObjectInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.process.ProcessInstance;
import at.fhjoanneum.ippr.persistence.objects.engine.subject.Subject;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectFieldInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.BusinessObjectInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.MessageFlowRepository;
import at.fhjoanneum.ippr.processengine.repositories.ProcessInstanceRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectRepository;
import at.fhjoanneum.ippr.processengine.repositories.SubjectStateRepository;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class ProcessEngineFeignServiceImpl implements ProcessEngineFeignService {

  private static final int MF_ID_INDEX = 2;
  private static final int S_ID_INDEX = 1;
  private static final int PI_ID_INDEX = 0;

  private final static Logger LOG = LoggerFactory.getLogger(ProcessEngineFeignServiceImpl.class);

  @Autowired
  private SubjectRepository subjectRepository;
  @Autowired
  private SubjectStateRepository subjectStateRepository;
  @Autowired
  private ProcessInstanceRepository processInstanceRepository;
  @Autowired
  private MessageFlowRepository messageFlowRepository;
  @Autowired
  private BusinessObjectInstanceRepository businessObjectInstanceRepository;
  @Autowired
  private BusinessObjectFieldInstanceRepository businessObjectFieldInstanceRepository;
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public void markAsSent(final String transferId) {
    final String[] split = transferId.split("-");
    final Long sId = Long.valueOf(split[S_ID_INDEX]);

    final Subject subject = subjectRepository.findOne(sId);
    subject.getSubjectState().setToSent();
    LOG.info("Marked as 'SENT' [{}]", subject.getSubjectState());

    subjectStateRepository.save((SubjectStateImpl) subject.getSubjectState());
  }

  @Override
  public void storeExternalCommunicatorMessage(final ExternalCommunicatorMessage message) {
    final String[] split = message.getTransferId().split("-");
    final Long piID = Long.valueOf(split[PI_ID_INDEX]);
    final Long mfId = Long.valueOf(split[MF_ID_INDEX]);
    final Long sId = Long.valueOf(split[S_ID_INDEX]);

    final ProcessInstance processInstance = processInstanceRepository.findOne(piID);
    final MessageFlow messageFlow = Optional.ofNullable(messageFlowRepository.findOne(mfId))
        .orElseThrow(() -> new IllegalArgumentException(
            "Could not find message flow for MF_ID [" + mfId + "]"));

    final Set<BusinessObjectInstance> businessObjectInstances =
        getBusinessObjectInstances(processInstance, messageFlow.getBusinessObjectModels());

    final Table<String, String, BusinessObjectField> records =
        convertToMap(message.getBusinessObjects());

    businessObjectInstances.stream()
        .forEachOrdered(objectInstance -> storeValues(records, objectInstance));

    final Subject subject = subjectRepository.findOne(sId);
    subject.getSubjectState().setToReceived(messageFlow);
    subjectRepository.save((SubjectImpl) subject);
  }

  private Set<BusinessObjectInstance> getBusinessObjectInstances(
      final ProcessInstance processInstance, final List<BusinessObjectModel> models) {
    final Set<BusinessObjectInstance> instances = new HashSet<>();
    models.stream()
        .forEachOrdered(model -> getBusinessObjectInstance(processInstance, model, instances));
    return instances;
  }

  private void getBusinessObjectInstance(final ProcessInstance processInstance,
      final BusinessObjectModel model, final Set<BusinessObjectInstance> instances) {
    if (processInstance.isBusinessObjectInstanceOfModelCreated(model)) {
      instances.add(businessObjectInstanceRepository
          .getBusinessObjectInstanceOfModelInProcess(processInstance.getPiId(), model.getBomId()));
    } else {
      final BusinessObjectInstance businessObjectInstance = new BusinessObjectInstanceBuilder()
          .processInstance(processInstance).businessObjectModel(model).build();
      final List<BusinessObjectFieldInstanceImpl> fields =
          model.getBusinessObjectFieldModels().stream()
              .map(fieldModel -> new BusinessObjectFieldInstanceBuilder()
                  .businessObjectInstance(businessObjectInstance)
                  .businessObjectFieldModel(fieldModel).build())
              .map(field -> (BusinessObjectFieldInstanceImpl) field).collect(Collectors.toList());

      businessObjectInstanceRepository.save((BusinessObjectInstanceImpl) businessObjectInstance);
      businessObjectFieldInstanceRepository.save(fields);
      entityManager.refresh(businessObjectInstance);
      instances.add(businessObjectInstance);
    }

    model.getChildren().stream()
        .forEachOrdered((child) -> getBusinessObjectInstance(processInstance, child, instances));
  }

  private Table<String, String, BusinessObjectField> convertToMap(
      final Set<BusinessObject> objects) {
    final Table<String, String, BusinessObjectField> records = HashBasedTable.create();

    objects.stream().forEachOrdered(object -> {
      object.getFields().stream()
          .forEachOrdered(field -> records.put(object.getName(), field.getName(), field));
    });

    return records;
  }

  private void storeValues(final Table<String, String, BusinessObjectField> records,
      final BusinessObjectInstance objectInstance) {
    final Map<String, BusinessObjectField> row =
        records.row(objectInstance.getBusinessObjectModel().getName());
    if (!row.isEmpty()) {
      objectInstance.getBusinessObjectFieldInstances().stream()
          .forEachOrdered(field -> storeValue(row, field));
    }
  }

  private void storeValue(final Map<String, BusinessObjectField> row,
      final BusinessObjectFieldInstance field) {
    final BusinessObjectField ecField = row.get(field.getBusinessObjectFieldModel().getFieldName());
    if (ecField != null) {
      field.setValue(ecField.getValue());
      businessObjectFieldInstanceRepository.save((BusinessObjectFieldInstanceImpl) field);
    }
  }
}
