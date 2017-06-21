package at.fhjoanneum.ippr.pmstorage.examples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
import at.fhjoanneum.ippr.persistence.objects.model.enums.SubjectModelType;
import at.fhjoanneum.ippr.persistence.objects.model.enums.TransitionType;
import at.fhjoanneum.ippr.persistence.objects.model.messageflow.MessageFlow;
import at.fhjoanneum.ippr.persistence.objects.model.process.ProcessModel;
import at.fhjoanneum.ippr.persistence.objects.model.state.State;
import at.fhjoanneum.ippr.persistence.objects.model.subject.SubjectModel;
import at.fhjoanneum.ippr.persistence.objects.model.transition.Transition;

@Component
@Transactional
@Order(1)
public class TravelRequestProcess extends AbstractExample {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  protected void createData() {
    final ProcessModel tm = createTravelManagement();

    final SubjectModel employee =
        new SubjectModelBuilder().name("MitarbeiterIn").addAssignedRule("EMPLOYEE_RULE").build();
    final SubjectModel boss =
        new SubjectModelBuilder().name("TeamleiterIn").addAssignedRule("BOSS_RULE").build();
    final SubjectModel travelMgt =
        new SubjectModelBuilder().name("Travel Management").type(SubjectModelType.PROCESS).build();

    // create vacation request
    final State empState1 = new StateBuilder().subjectModel(employee).name("Reiseantrag anlegen")
        .eventType(StateEventType.START).functionType(StateFunctionType.FUNCTION).build();

    // send vacation request
    final State empState2 = new StateBuilder().subjectModel(employee).name("Reiseantrag senden")
        .functionType(StateFunctionType.SEND).build();
    final Transition empT1 =
        new TransitionBuilder().fromState(empState1).toState(empState2).build();

    // receive vacation request
    final State bossState1 = new StateBuilder().subjectModel(boss).name("Empfange Reiseantrag")
        .eventType(StateEventType.START).functionType(StateFunctionType.RECEIVE).build();


    // accept or do not accept vacation request
    final State bossState2 = new StateBuilder().subjectModel(boss).name("Reiseantrag prüfen")
        .functionType(StateFunctionType.FUNCTION).build();


    final Transition bossT1 =
        new TransitionBuilder().fromState(bossState1).toState(bossState2).build();
    final State bossState3 = new StateBuilder().subjectModel(boss).name("Reiseantrag annehmen")
        .functionType(StateFunctionType.SEND).build();
    final Transition bossT2 =
        new TransitionBuilder().fromState(bossState2).toState(bossState3).build();
    final State bossState4 = new StateBuilder().subjectModel(boss).name("Reiseantrag ablehnen")
        .functionType(StateFunctionType.SEND).build();
    final Transition bossT3 =
        new TransitionBuilder().fromState(bossState2).toState(bossState4).build();

    // finish the boss
    final State bossState5 = new StateBuilder().subjectModel(boss).name("Ende")
        .functionType(StateFunctionType.FUNCTION).eventType(StateEventType.END).build();
    final Transition bossT4 =
        new TransitionBuilder().fromState(bossState3).toState(bossState5).build();
    final Transition bossT5 =
        new TransitionBuilder().fromState(bossState4).toState(bossState5).build();

    // receive the response from the boss
    final State empState3 = new StateBuilder().subjectModel(employee)
        .name("Empfange Reiseantrag Antwort").functionType(StateFunctionType.RECEIVE).build();
    final Transition empT2 =
        new TransitionBuilder().fromState(empState2).toState(empState3).build();

    final State empState4 = new StateBuilder().subjectModel(employee).name("Reiseantrag angenommen")
        .functionType(StateFunctionType.FUNCTION).build();
    final State empState5 = new StateBuilder().subjectModel(employee)
        .name("Reiseantrag nicht angenommen").functionType(StateFunctionType.FUNCTION).build();

    final Transition if1 = new TransitionBuilder().fromState(empState3).toState(empState4)
        .transitionType(TransitionType.NORMAL).build();
    final Transition if2 = new TransitionBuilder().fromState(empState3).toState(empState5)
        .transitionType(TransitionType.NORMAL).build();

    final State empState6 = new StateBuilder().subjectModel(employee)
        .name("Sende an Travel Management").functionType(StateFunctionType.SEND).build();
    final Transition empT3 =
        new TransitionBuilder().fromState(empState4).toState(empState6).build();

    final State empState7 = new StateBuilder().subjectModel(employee).name("Empfange Hotelbuchung")
        .functionType(StateFunctionType.RECEIVE).build();
    final Transition empT6 =
        new TransitionBuilder().fromState(empState6).toState(empState7).build();

    final BusinessObjectModel hotel =
        new BusinessObjectModelBuilder().name("Hotelbuchung").addToState(empState7).build();
    final BusinessObjectFieldModel hotelF1 =
        new BusinessObjectFieldModelBuilder().businessObjectModel(hotel).fieldName("Name")
            .fieldType(FieldType.STRING).position(0).build();
    final BusinessObjectFieldModel hotelF2 =
        new BusinessObjectFieldModelBuilder().businessObjectModel(hotel).fieldName("Sterne")
            .fieldType(FieldType.STRING).position(1).build();

    final BusinessObjectFieldPermission hotelP1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(hotelF1)
            .permission(FieldPermission.READ).state(empState7).build();
    final BusinessObjectFieldPermission hotelP2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(hotelF2)
            .permission(FieldPermission.READ).state(empState7).build();

    // finish the employee
    final State empState8 = new StateBuilder().subjectModel(employee).name("Ende")
        .eventType(StateEventType.END).functionType(StateFunctionType.FUNCTION).build();

    final Transition empt8 =
        new TransitionBuilder().fromState(empState7).toState(empState8).build();
    final Transition empT4 =
        new TransitionBuilder().fromState(empState5).toState(empState8).build();

    final BusinessObjectModel vacationRequestForm = new BusinessObjectModelBuilder()
        .name("Reiseantrag").addToState(empState1).addToState(empState2).addToState(bossState1)
        .addToState(bossState2).addToState(empState3).addToState(empState4).build();

    final BusinessObjectFieldModel boFrom = new BusinessObjectFieldModelBuilder().fieldName("Von")
        .fieldType(FieldType.STRING).businessObjectModel(vacationRequestForm).position(0).build();
    final BusinessObjectFieldModel boTo = new BusinessObjectFieldModelBuilder().fieldName("Bis")
        .fieldType(FieldType.STRING).businessObjectModel(vacationRequestForm).position(1).build();
    final BusinessObjectFieldModel boLocation =
        new BusinessObjectFieldModelBuilder().fieldName("Ort").fieldType(FieldType.STRING)
            .businessObjectModel(vacationRequestForm).position(2).build();

    final BusinessObjectFieldPermission boFromPermissionEmp1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom).state(empState1)
            .permission(FieldPermission.READ_WRITE).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionEmp2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(empState1)
            .permission(FieldPermission.READ_WRITE).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionEmp3 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom).state(empState2)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionEmp4 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(empState2)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionEmp5 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(empState1).permission(FieldPermission.READ_WRITE).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionEmp6 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(empState2).permission(FieldPermission.READ).mandatory(true).build();

    final BusinessObjectFieldPermission boFromPermissionBoss1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom)
            .state(bossState1).permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(bossState1)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss3 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(bossState1).permission(FieldPermission.READ).mandatory(true).build();

    final BusinessObjectFieldPermission boFromPermissionBoss4 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom)
            .state(bossState2).permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss5 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(bossState2)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss6 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(bossState2).permission(FieldPermission.READ).mandatory(true).build();


    final BusinessObjectModel okForm = new BusinessObjectModelBuilder().name("Reiseantragannahme")
        .addToState(bossState3).addToState(empState3).addToState(empState4).build();
    final BusinessObjectFieldModel okFormFieldInformation =
        new BusinessObjectFieldModelBuilder().businessObjectModel(okForm).fieldName("Information")
            .fieldType(FieldType.STRING).position(0).build();
    final BusinessObjectFieldPermission okFormFieldInformationPermission1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(okFormFieldInformation)
            .state(bossState3).mandatory(false).permission(FieldPermission.READ_WRITE).build();
    final BusinessObjectFieldPermission okFormFieldInformationPermission2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(okFormFieldInformation)
            .state(empState3).mandatory(false).permission(FieldPermission.READ).build();
    final BusinessObjectFieldPermission okFormFieldInformationPermission3 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(okFormFieldInformation)
            .state(empState4).mandatory(false).permission(FieldPermission.READ).build();

    final BusinessObjectFieldPermission boFromPermissionBoss7 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom).state(empState3)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss8 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(empState3)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss9 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(empState3).permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss10 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom).state(empState4)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss11 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(empState4)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss12 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(empState4).permission(FieldPermission.READ).mandatory(true).build();


    final BusinessObjectModel nokForm =
        new BusinessObjectModelBuilder().name("Reiseantragablehnung").addToState(bossState4)
            .addToState(empState3).addToState(empState5).build();
    final BusinessObjectFieldModel nokFormFieldInformation =
        new BusinessObjectFieldModelBuilder().businessObjectModel(nokForm).fieldName("Information")
            .fieldType(FieldType.STRING).position(0).build();
    final BusinessObjectFieldPermission nokFormFieldInformationPermission1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(nokFormFieldInformation)
            .state(bossState4).mandatory(true).permission(FieldPermission.READ_WRITE).build();
    final BusinessObjectFieldPermission nokFormFieldInformationPermission2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(nokFormFieldInformation)
            .state(empState3).mandatory(false).permission(FieldPermission.READ).build();
    final BusinessObjectFieldPermission nokFormFieldInformationPermission3 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(nokFormFieldInformation)
            .state(empState5).mandatory(false).permission(FieldPermission.READ).build();

    final BusinessObjectFieldPermission boFromPermissionBoss13 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom).state(empState5)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss14 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo).state(empState5)
            .permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission boFromPermissionBoss15 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(empState5).permission(FieldPermission.READ).mandatory(true).build();


    final ProcessModel pm = new ProcessModelBuilder().name("Reiseantrag mit Prozesskommunikation")
        .description("Beispielprozess 1").state(ProcessModelState.ACTIVE).addSubjectModel(boss)
        .addSubjectModel(employee).addSubjectModel(travelMgt).starterSubject(employee).version(1.1F)
        .build();

    final MessageFlow mf1 = new MessageFlowBuilder().sender(employee).receiver(boss)
        .state(empState2).assignBusinessObjectModel(vacationRequestForm).build();
    final MessageFlow mf4 = new MessageFlowBuilder().sender(employee).receiver(boss)
        .state(bossState1).assignBusinessObjectModel(vacationRequestForm).build();

    final MessageFlow mf2 = new MessageFlowBuilder().sender(boss).receiver(employee)
        .state(bossState3).assignBusinessObjectModel(okForm).build();
    final MessageFlow mf5 = new MessageFlowBuilder().sender(boss).receiver(employee)
        .state(empState3).assignBusinessObjectModel(okForm).build();

    final MessageFlow mf3 = new MessageFlowBuilder().sender(boss).receiver(employee)
        .state(bossState4).assignBusinessObjectModel(nokForm).build();
    final MessageFlow mf6 = new MessageFlowBuilder().sender(boss).receiver(employee)
        .state(empState3).assignBusinessObjectModel(nokForm).build();

    final MessageFlow mf7 =
        new MessageFlowBuilder().sender(employee).receiver(travelMgt).state(empState6)
            .assignBusinessObjectModel(vacationRequestForm).assignProcessModel(tm).build();

    final MessageFlow mf8 = new MessageFlowBuilder().sender(travelMgt).receiver(employee)
        .state(empState7).assignBusinessObjectModel(hotel).build();

    saveSubjectModels(boss, employee, travelMgt);
    saveProcessModel(pm);

    saveStates(empState1, empState2, empState3, empState4, empState5, empState8, bossState1,
        bossState2, bossState3, bossState4, bossState5, empState6, empState7);
    saveTransitions(empT1, empT2, empT3, empT4, bossT1, bossT2, bossT3, bossT4, bossT5, if1, if2,
        empt8, empT6);

    saveBusinessObjectModels(vacationRequestForm, okForm, nokForm, hotel);
    saveBusinessObjectFieldModels(boFrom, boTo, nokFormFieldInformation, okFormFieldInformation,
        hotelF1, hotelF2, boLocation);
    saveBusinessObjectFieldPermissions(boFromPermissionEmp1, boFromPermissionEmp2,
        boFromPermissionBoss1, boFromPermissionBoss2, okFormFieldInformationPermission1,
        okFormFieldInformationPermission2, nokFormFieldInformationPermission1,
        nokFormFieldInformationPermission2, boFromPermissionEmp3, boFromPermissionEmp4,
        okFormFieldInformationPermission3, nokFormFieldInformationPermission3, hotelP1, hotelP2,
        boFromPermissionEmp5, boFromPermissionEmp6, boFromPermissionBoss3, boFromPermissionBoss4,
        boFromPermissionBoss5, boFromPermissionBoss6, boFromPermissionBoss7, boFromPermissionBoss8,
        boFromPermissionBoss9, boFromPermissionBoss10, boFromPermissionBoss11,
        boFromPermissionBoss12, boFromPermissionBoss13, boFromPermissionBoss14,
        boFromPermissionBoss15);
    saveMessageFlows(mf1, mf2, mf3, mf4, mf5, mf6, mf7, mf8);
  }

  private ProcessModel createTravelManagement() {
    final SubjectModel employee =
        new SubjectModelBuilder().name("MitarbeiterIn").type(SubjectModelType.PROCESS).build();
    final SubjectModel travelMgt =
        new SubjectModelBuilder().name("Travel Management").addAssignedRule("TRAVEL_RULE").build();

    // receive travel info
    final State receiveTravelInfo =
        new StateBuilder().subjectModel(travelMgt).name("Empfange Reisedaten")
            .eventType(StateEventType.START).functionType(StateFunctionType.RECEIVE).build();

    final BusinessObjectModel travelInfoForm =
        new BusinessObjectModelBuilder().name("Reiseantrag").addToState(receiveTravelInfo).build();
    final BusinessObjectFieldModel boFrom = new BusinessObjectFieldModelBuilder().fieldName("Von")
        .fieldType(FieldType.STRING).businessObjectModel(travelInfoForm).position(0).build();
    final BusinessObjectFieldModel boTo = new BusinessObjectFieldModelBuilder().fieldName("Bis")
        .fieldType(FieldType.STRING).businessObjectModel(travelInfoForm).position(1).build();
    final BusinessObjectFieldModel boLocation =
        new BusinessObjectFieldModelBuilder().fieldName("Ort").fieldType(FieldType.STRING)
            .businessObjectModel(travelInfoForm).position(2).build();

    final BusinessObjectFieldPermission travelInfoFormPerm1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boFrom)
            .state(receiveTravelInfo).permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission travelInfoFormPerm2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boTo)
            .state(receiveTravelInfo).permission(FieldPermission.READ).mandatory(true).build();
    final BusinessObjectFieldPermission travelInfoFormPerm3 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(boLocation)
            .state(receiveTravelInfo).permission(FieldPermission.READ).mandatory(true).build();


    final MessageFlow mf1 = new MessageFlowBuilder().sender(employee).receiver(travelMgt)
        .state(receiveTravelInfo).assignBusinessObjectModel(travelInfoForm).build();

    // enter hotel information
    final State enterHotelInfo = new StateBuilder().subjectModel(travelMgt)
        .name("Hotelbuchung durchführen").functionType(StateFunctionType.SEND).build();

    final BusinessObjectModel hotel =
        new BusinessObjectModelBuilder().name("Hotelbuchung").addToState(enterHotelInfo).build();
    final BusinessObjectFieldModel hotelF1 =
        new BusinessObjectFieldModelBuilder().businessObjectModel(hotel).fieldName("Name")
            .fieldType(FieldType.STRING).position(0).build();
    final BusinessObjectFieldModel hotelF2 =
        new BusinessObjectFieldModelBuilder().businessObjectModel(hotel).fieldName("Sterne")
            .fieldType(FieldType.STRING).position(1).build();

    final BusinessObjectFieldPermission hotelP1 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(hotelF1)
            .permission(FieldPermission.READ_WRITE).state(enterHotelInfo).build();
    final BusinessObjectFieldPermission hotelP2 =
        new BusinessObjectFieldPermissionBuilder().businessObjectFieldModel(hotelF2)
            .permission(FieldPermission.READ_WRITE).state(enterHotelInfo).build();

    final MessageFlow mf2 = new MessageFlowBuilder().sender(travelMgt).receiver(employee)
        .state(enterHotelInfo).assignBusinessObjectModel(hotel).build();

    // end
    final State end = new StateBuilder().subjectModel(travelMgt).name("Ende")
        .eventType(StateEventType.END).functionType(StateFunctionType.FUNCTION).build();

    final Transition t1 =
        new TransitionBuilder().fromState(receiveTravelInfo).toState(enterHotelInfo).build();
    final Transition t2 = new TransitionBuilder().fromState(enterHotelInfo).toState(end).build();

    final ProcessModel tm = new ProcessModelBuilder().name("Travel Management Buchungsprozess")
        .description("Beispielprozess 3").state(ProcessModelState.ACTIVE).addSubjectModel(travelMgt)
        .addSubjectModel(employee).starterSubject(travelMgt).version(1.0F).build();

    saveSubjectModels(employee, travelMgt);
    saveProcessModel(tm);
    saveStates(receiveTravelInfo, enterHotelInfo, end);
    saveTransitions(t1, t2);
    saveBusinessObjectModels(travelInfoForm, hotel);
    saveBusinessObjectFieldModels(boFrom, boTo, hotelF1, hotelF2, boLocation);
    saveBusinessObjectFieldPermissions(travelInfoFormPerm1, travelInfoFormPerm2, hotelP1, hotelP2,
        travelInfoFormPerm3);
    saveMessageFlows(mf1, mf2);

    return tm;
  }

  @Override
  protected String getName() {
    return "Travel Request with external process";
  }

}
