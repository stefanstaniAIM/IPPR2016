package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class OWLMessageFlowDTO implements Serializable {

  private String senderId;
  private String receiverId;
  private String bomId;
  private String id;
  private String senderStateId;
  private String receiverStateId;


  public OWLMessageFlowDTO() {}

  public OWLMessageFlowDTO(final String id, final String senderId, final String receiverId,
      final String bomId, final String senderStateId, final String receiverStateId) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.bomId = bomId;
    this.id = id;
    this.senderStateId = senderStateId;
    this.receiverStateId = receiverStateId;
  }

  public String getId() {
    return id;
  }

  public String getSenderId() {
    return senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public String getBomId() {
    return bomId;
  }

  public String getSenderStateId() {
    return senderStateId;
  }

  public String getReceiverStateId() {
    return receiverStateId;
  }
}
