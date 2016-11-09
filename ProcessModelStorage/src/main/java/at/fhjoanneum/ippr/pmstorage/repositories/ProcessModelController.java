package at.fhjoanneum.ippr.pmstorage.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;

import at.fhjoanneum.ippr.persistence.entities.model.businessobject.BusinessObjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.field.BusinessObjectFieldModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.businessobject.permission.BusinessObjectFieldPermissionBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.messageflow.MessageFlowBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.process.ProcessModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.state.StateBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.subject.SubjectModelBuilder;
import at.fhjoanneum.ippr.persistence.entities.model.transition.TransitionBuilder;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.BusinessObjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.field.BusinessObjectFieldModel;
import at.fhjoanneum.ippr.persistence.objects.model.businessobject.permission.BusinessObjectFieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldPermission;
import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.ProcessModelState;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateEventType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.StateFunctionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

@Controller
public class ProcessModelController {

  private static final Logger LOG = LoggerFactory.getLogger(ProcessModelBuilder.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void create() {
    final SubjectModel maSubject =
        new SubjectModelBuilder().name("Mitarbeiter").assignedGroup("MA").build();
    final SubjectModel chefSubject =
        new SubjectModelBuilder().name("Chef").assignedGroup("BOSS").build();

    // MA
    final State ma1 = new StateBuilder().subjectModel(maSubject).name("Urlaubsantrag ausfuellen")
        .eventType(StateEventType.START).functionType(StateFunctionType.FUNCTION).build();
    final State ma2 = new StateBuilder().subjectModel(maSubject)
        .name("Urlaubsantrag an Chef senden").functionType(StateFunctionType.SEND).build();
    final Transition t1 = new TransitionBuilder().fromState(ma1).toState(ma2).build();
    final MessageFlow mf1 =
        new MessageFlowBuilder().sender(maSubject).receiver(chefSubject).state(ma2).build();

    final State ma3 = new StateBuilder().subjectModel(maSubject).name("Warte auf Genehmigung")
        .functionType(StateFunctionType.RECEIVE).build();
    final Transition t2 = new TransitionBuilder().fromState(ma2).toState(ma3).build();

    final State ma4 = new StateBuilder().subjectModel(maSubject).name("END")
        .functionType(StateFunctionType.FUNCTION).eventType(StateEventType.END).build();
    final Transition t3 = new TransitionBuilder().fromState(ma3).toState(ma4).build();

    // CHEF
    final State c1 = new StateBuilder().subjectModel(chefSubject).name("Warte auf Urlaubsantrag")
        .eventType(StateEventType.START).functionType(StateFunctionType.RECEIVE).build();
    final State c2 = new StateBuilder().subjectModel(chefSubject).name("Genehmigung OK/NOK")
        .functionType(StateFunctionType.FUNCTION).build();
    final Transition t4 = new TransitionBuilder().fromState(c1).toState(c2).build();

    final State c3 = new StateBuilder().subjectModel(chefSubject).name("Urlaub OK")
        .functionType(StateFunctionType.SEND).build();
    final Transition t5 = new TransitionBuilder().fromState(c2).toState(c3).build();
    final MessageFlow mf2 =
        new MessageFlowBuilder().sender(chefSubject).receiver(maSubject).state(c3).build();

    final State c4 = new StateBuilder().subjectModel(chefSubject).name("Urlaub NOK")
        .functionType(StateFunctionType.SEND).build();
    final Transition t6 = new TransitionBuilder().fromState(c2).toState(c4).build();
    final MessageFlow mf3 =
        new MessageFlowBuilder().sender(chefSubject).receiver(maSubject).state(c4).build();

    final State c5 = new StateBuilder().subjectModel(chefSubject).name("END")
        .functionType(StateFunctionType.FUNCTION).eventType(StateEventType.END).build();
    final Transition t7 = new TransitionBuilder().fromState(c3).toState(c5).build();
    final Transition t8 = new TransitionBuilder().fromState(c4).toState(c5).build();

    final ProcessModel pm = new ProcessModelBuilder().name("Urlaubsantrag")
        .description("Prozess fuer Beantragung des Urlaubs").state(ProcessModelState.ACTIVE)
        .subjectModels(Lists.newArrayList(maSubject, chefSubject)).starterSubject(maSubject)
        .build();

    final BusinessObjectModel boAntrag = new BusinessObjectModelBuilder().name("Urlaubsantrag")
        .addToState(ma1).addToState(ma2).build();
    final BusinessObjectFieldModel boAntragField = new BusinessObjectFieldModelBuilder()
        .fieldName("Zeitraum").fieldType(FieldType.STRING).businessObjectModel(boAntrag).build();
    final BusinessObjectFieldPermission bofp =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boAntragField)
            .state(ma1).permission(FieldPermission.READ_WRITE).mandatory(true).build();

    saveSubjectModels(chefSubject, maSubject);
    saveProcessModel(pm);
    saveStates(ma1, ma2, ma3, ma4, c1, c2, c3, c4, c5);
    saveMessageFlows(mf1, mf2, mf3);
    saveTransitions(t1, t2, t3, t4, t5, t6, t7, t8);
    saveBusinessObjectModels(boAntrag);
    saveBusinessObjectFieldModels(boAntragField);
    saveBusinessObjectFieldPermissions(bofp);
  }

  private void saveProcessModel(final ProcessModel processModel) {
    entityManager.persist(processModel);
    LOG.info("Saved new process model: {}", processModel);
  }

  private void saveSubjectModels(final SubjectModel... models) {
    for (final SubjectModel model : models) {
      entityManager.persist(model);
      LOG.info("Saved new subject model: {}", model);
    }
  }

  private void saveStates(final State... states) {
    for (final State state : states) {
      entityManager.persist(state);
      LOG.info("Saved new state: {}", state);
    }
  }

  private void saveMessageFlows(final MessageFlow... messageFlows) {
    for (final MessageFlow messageFlow : messageFlows) {
      entityManager.persist(messageFlow);
      LOG.info("Saved new message flow: {}", messageFlow);
    }
  }

  private void saveTransitions(final Transition... transitions) {
    for (final Transition transition : transitions) {
      entityManager.persist(transition);
      LOG.info("Saved new transition: {}", transition);
    }
  }

  private void saveBusinessObjectModels(final BusinessObjectModel... businessObjectModels) {
    for (final BusinessObjectModel businessObjectModel : businessObjectModels) {
      entityManager.persist(businessObjectModel);
      LOG.info("Saved new business object model: {}", businessObjectModel);
    }
  }

  private void saveBusinessObjectFieldModels(
      final BusinessObjectFieldModel... businessObjectFieldModels) {
    for (final BusinessObjectFieldModel businessObjectFieldModel : businessObjectFieldModels) {
      entityManager.persist(businessObjectFieldModel);
      LOG.info("Saved new business object field model: {}", businessObjectFieldModel);
    }
  }

  private void saveBusinessObjectFieldPermissions(
      final BusinessObjectFieldPermission... businessObjectFieldPermissions) {
    for (final BusinessObjectFieldPermission businessObjectFieldPermission : businessObjectFieldPermissions) {
      entityManager.persist(businessObjectFieldPermission);
      LOG.info("Saved new business object field permission: {}", businessObjectFieldPermission);
    }
  }
}
