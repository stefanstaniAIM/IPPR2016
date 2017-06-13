package at.fhjoanneum.ippr.pmstorage.examples;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

@Transactional
public abstract class AbstractExample implements ApplicationRunner {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractExample.class);

  @Autowired
  private ExampleConfiguration exampleConfiguration;

  protected abstract EntityManager getEntityManager();

  protected abstract void createData();

  protected abstract String getName();

  @Override
  public void run(final ApplicationArguments args) throws Exception {
    if (exampleConfiguration.isInsertExamplesEnabled()) {
      LOG.info(
          "#######################################################################################################");
      LOG.info("Test data for example process: {}", getName());
      createData();
      LOG.info(
          "#######################################################################################################");
    } else {
      LOG.info("Examples won't be inserted since 'ippr.insert-examples.enabled' is false");
    }
  }

  protected void saveProcessModel(final ProcessModel processModel) {
    getEntityManager().persist(processModel);
    LOG.info("Saved new process model: {}", processModel);
  }

  protected void saveSubjectModels(final SubjectModel... models) {
    for (final SubjectModel model : models) {
      getEntityManager().persist(model);
      LOG.info("Saved new subject model: {}", model);
    }
  }

  protected void saveStates(final State... states) {
    for (final State state : states) {
      getEntityManager().persist(state);
      LOG.info("Saved new state: {}", state);
    }
  }

  protected void saveMessageFlows(final MessageFlow... messageFlows) {
    for (final MessageFlow messageFlow : messageFlows) {
      getEntityManager().persist(messageFlow);
      LOG.info("Saved new message flow: {}", messageFlow);
    }
  }

  protected void saveTransitions(final Transition... transitions) {
    for (final Transition transition : transitions) {
      getEntityManager().persist(transition);
      LOG.info("Saved new transition: {}", transition);
    }
  }

  protected void saveBusinessObjectModels(final BusinessObjectModel... businessObjectModels) {
    for (final BusinessObjectModel businessObjectModel : businessObjectModels) {
      getEntityManager().persist(businessObjectModel);
      LOG.info("Saved new business object model: {}", businessObjectModel);
    }
  }

  protected void saveBusinessObjectFieldModels(
      final BusinessObjectFieldModel... businessObjectFieldModels) {
    for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectFieldModels) {
      getEntityManager().persist(businessObjectFieldModel);
      LOG.info("Saved new business object field model: {}", businessObjectFieldModel);
    }
  }

  protected void saveBusinessObjectFieldPermissions(
      final BusinessObjectFieldPermission... businessObjectFieldPermissions) {
    for (final BusinessObjectFieldPermission businessObjectFieldPermission : businessObjectFieldPermissions) {
      getEntityManager().persist(businessObjectFieldPermission);
      LOG.info("Saved new business object field permission: {}", businessObjectFieldPermission);
    }
  }
}
