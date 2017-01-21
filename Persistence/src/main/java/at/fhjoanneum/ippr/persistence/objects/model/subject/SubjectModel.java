package at.fhjoanneum.ippr.persistence.objects.model.subject;

import java.util.List;

import at.fhjoanneum.ippr.persistence.objects.model.state.State;

public interface SubjectModel {

  Long getSmId();

  String getName();

  String getGroup();

  List<State> getStates();

  List<String> getAssignedRules();
}
