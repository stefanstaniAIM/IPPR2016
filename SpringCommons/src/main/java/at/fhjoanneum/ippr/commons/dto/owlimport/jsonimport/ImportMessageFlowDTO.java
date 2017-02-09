package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;

public class ImportMessageFlowDTO implements Serializable {

  private static final long serialVersionUID = 618975441007965912L;

  private String id;
  private String bomId;
  private String senderId;
  private String receiverId;
  private String stateId;

  public ImportMessageFlowDTO() {}

  public String getId() {
    return id;
  }

  public String getBomId() {
    return bomId;
  }

  public String getSenderId() {
    return senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public String getStateId() {
    return stateId;
  }
}
