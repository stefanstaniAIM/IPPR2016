package at.fhjoanneum.ippr.pmstorage.examples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

@Component
@Transactional
public class VacationRequest extends AbstractExample {

  private static final Logger LOG = LoggerFactory.getLogger(VacationRequest.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  protected void createData() {
    final SubjectModel employee =
        new SubjectModelBuilder().name("Employee").assignedGroup("Employee").build();
    final SubjectModel boss = new SubjectModelBuilder().name("Boss").assignedGroup("Boss").build();

    // create vacation request
    final State empState1 =
        new StateBuilder().subjectModel(employee).name("Create vacation request")
            .eventType(StateEventType.START).functionType(StateFunctionType.FUNCTION).build();

    // send vacation request
    final State empState2 = new StateBuilder().subjectModel(employee).name("Send vacation request")
        .functionType(StateFunctionType.SEND).build();
    final Transition empT1 =
        new TransitionBuilder().fromState(empState1).toState(empState2).build();
    final MessageFlow mf1 =
        new MessageFlowBuilder().sender(employee).receiver(boss).state(empState2).build();

    // receive vacation request
    final State bossState1 = new StateBuilder().subjectModel(boss).name("Receive vacation request")
        .eventType(StateEventType.START).functionType(StateFunctionType.RECEIVE).build();

    final BusinessObjectModel vacationRequestForm = new BusinessObjectModelBuilder()
        .name("Vacation request form").addToState(empState1).addToState(bossState1).build();
    final BusinessObjectFieldModel boFrom = new BusinessObjectFieldModelBuilder().fieldName("From")
        .fieldType(FieldType.STRING).businessObjectModel(vacationRequestForm).build();
    final BusinessObjectFieldModel boTo = new BusinessObjectFieldModelBuilder().fieldName("To")
        .fieldType(FieldType.STRING).businessObjectModel(vacationRequestForm).build();

    final BusinessObjectFieldPermission boFromPermissionEmp1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom).state(empState1)
            .permission(FieldPermission.READ_WRITE).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionEmp2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(empState1)
            .permission(FieldPermission.READ_WRITE).mandatory(true).build();

    final BusinessObjectFieldPermission boFromPermissionBoss1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom)
            .state(bossState1).permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(bossState1)
            .permission(FieldPermission.READ).mandatory(true).build();

    // accept or do not accept vacation request
    final State bossState2 = new StateBuilder().subjectModel(boss)
        .name("Vacation request OK or NOK").functionType(StateFunctionType.FUNCTION).build();
    final Transition bossT1 =
        new TransitionBuilder().fromState(bossState1).toState(bossState2).build();
    final State bossState3 = new StateBuilder().subjectModel(boss).name("Vacation request OK")
        .functionType(StateFunctionType.SEND).build();
    final Transition bossT2 =
        new TransitionBuilder().fromState(bossState2).toState(bossState3).build();
    final State bossState4 = new StateBuilder().subjectModel(boss).name("Vacation request NOK")
        .functionType(StateFunctionType.SEND).build();
    final Transition bossT3 =
        new TransitionBuilder().fromState(bossState2).toState(bossState4).build();
    final MessageFlow mf2 =
        new MessageFlowBuilder().sender(boss).receiver(employee).state(bossState3).build();
    final MessageFlow mf3 =
        new MessageFlowBuilder().sender(boss).receiver(employee).state(bossState4).build();

    // finish the boss
    final State bossState5 = new StateBuilder().subjectModel(boss).name("END")
        .functionType(StateFunctionType.FUNCTION).eventType(StateEventType.END).build();
    final Transition bossT4 =
        new TransitionBuilder().fromState(bossState3).toState(bossState5).build();
    final Transition bossT5 =
        new TransitionBuilder().fromState(bossState4).toState(bossState5).build();

    // receive the response from the boss
    final State empState3 = new StateBuilder().subjectModel(employee)
        .name("Receive vacation request response").functionType(StateFunctionType.RECEIVE).build();
    final Transition empT2 =
        new TransitionBuilder().fromState(empState2).toState(empState3).build();

    final BusinessObjectModel okForm = new BusinessObjectModelBuilder()
        .name("Vacation request accept").addToState(bossState2).addToState(empState3).build();
    final BusinessObjectFieldModel okFormFieldInformation = new BusinessObjectFieldModelBuilder()
        .businessObjectModel(okForm).fieldName("Information").fieldType(FieldType.STRING).build();
    final BusinessObjectFieldPermission okFormFieldInformationPermission1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(okFormFieldInformation)
            .state(bossState2).mandatory(false).permission(FieldPermission.READ_WRITE).build();
    final BusinessObjectFieldPermission okFormFieldInformationPermission2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(okFormFieldInformation)
            .state(empState3).mandatory(false).permission(FieldPermission.NONE).build();

    final BusinessObjectModel nokForm = new BusinessObjectModelBuilder()
        .name("Vacation request accept").addToState(bossState3).addToState(empState3).build();
    final BusinessObjectFieldModel nokFormFieldInformation = new BusinessObjectFieldModelBuilder()
        .businessObjectModel(nokForm).fieldName("Information").fieldType(FieldType.STRING).build();
    final BusinessObjectFieldPermission nokFormFieldInformationPermission1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(nokFormFieldInformation)
            .state(bossState2).mandatory(true).permission(FieldPermission.READ_WRITE).build();
    final BusinessObjectFieldPermission nokFormFieldInformationPermission2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(nokFormFieldInformation)
            .state(empState3).mandatory(false).permission(FieldPermission.READ).build();

    // finish the employee
    final State empState4 = new StateBuilder().subjectModel(employee).name("END")
        .functionType(StateFunctionType.FUNCTION).build();
    final Transition empT3 =
        new TransitionBuilder().fromState(empState3).toState(empState4).build();

    final ProcessModel pm =
        new ProcessModelBuilder().name("Vacation request").description("Request for vacation")
            .state(ProcessModelState.ACTIVE).subjectModels(Lists.newArrayList(boss, employee))
            .starterSubject(employee).version(1.1F).build();

    saveSubjectModels(boss, employee);
    saveProcessModel(pm);

    saveStates(empState1, empState2, empState3, empState4, bossState1, bossState2, bossState3,
        bossState4, bossState5);
    saveMessageFlows(mf1, mf2, mf3);
    saveTransitions(empT1, empT2, empT3, bossT1, bossT2, bossT3, bossT4, bossT5);

    saveBusinessObjectModels(vacationRequestForm, okForm, nokForm);
    saveBusinessObjectFieldModels(boFrom, boTo, nokFormFieldInformation, okFormFieldInformation);
    saveBusinessObjectFieldPermissions(boFromPermissionEmp1, boFromPermissionEmp2,
        boFromPermissionBoss1, boFromPermissionBoss2, okFormFieldInformationPermission1,
        okFormFieldInformationPermission2, nokFormFieldInformationPermission1,
        nokFormFieldInformationPermission2);
  }

  @Override
  protected String getName() {
    return "Vacation Request";
  }
}
