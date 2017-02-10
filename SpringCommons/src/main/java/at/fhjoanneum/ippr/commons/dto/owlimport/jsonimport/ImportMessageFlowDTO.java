package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;

public class ImportMessageFlowDTO implements Serializable {

  private static final long serialVersionUID = 618975441007965912L;

  private String id;
  private String bomId;
  private String senderId;
  private String receiverId;
  private String senderStateId;
  private String receiverStateId;

  public ImportMessageFlowDTO() {}

  public ImportMessageFlowDTO(final String id, final String bomId, final String senderId,
      final String receiverId, final String senderStateId, final String receiverStateId) {
    this.id = id;
    this.bomId = bomId;
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.senderStateId = senderStateId;
    this.receiverStateId = receiverStateId;
  }

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

  public String getSenderStateId() {
    return senderStateId;
  }

  public String getReceiverStateId() {
    return receiverStateId;
  }
}
