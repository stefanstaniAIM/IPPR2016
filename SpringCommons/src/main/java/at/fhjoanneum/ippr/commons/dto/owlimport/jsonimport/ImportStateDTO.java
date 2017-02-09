package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;

public class ImportStateDTO implements Serializable {

  private static final long serialVersionUID = 6992880442627153867L;

  private String id;
  private String name;
  private String functionType;
  private String eventType;
  private String subjectModelId;

  public ImportStateDTO() {}

  public ImportStateDTO(final String id, final String name, final String functionType,
      final String eventType, final String subjectModelId) {
    this.id = id;
    this.name = name;
    this.functionType = functionType;
    this.eventType = eventType;
    this.subjectModelId = subjectModelId;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getFunctionType() {
    return functionType;
  }

  public String getEventType() {
    return eventType;
  }

  public String getSubjectModelId() {
    return subjectModelId;
  }
}
