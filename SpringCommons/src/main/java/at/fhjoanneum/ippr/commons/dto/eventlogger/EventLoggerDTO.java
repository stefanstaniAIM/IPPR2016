package at.fhjoanneum.ippr.commons.dto.eventlogger;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class EventLoggerDTO implements Serializable {

  private Long eventId;
  private Long caseId;
  private Long processModelId;
  private String timestamp;
  private String activity;
  private String resource;
  private String state;
  private String messageType;
  private String recipient;
  private String sender;

  public EventLoggerDTO() {}

  public EventLoggerDTO(Long eventId, Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType, String recipient, String sender) {
    this.eventId = eventId;
    this.caseId = caseId;
    this.processModelId = processModelId;
    this.timestamp = timestamp;
    this.activity = activity;
    this.resource = resource;
    this.state = state;
    this.messageType = messageType;
    this.recipient = recipient;
    this.sender = sender;
  }

  public EventLoggerDTO(Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType, String recipient, String sender) {
    this.caseId = caseId;
    this.processModelId = processModelId;
    this.timestamp = timestamp;
    this.activity = activity;
    this.resource = resource;
    this.state = state;
    this.messageType = messageType;
    this.recipient = recipient;
    this.sender = sender;
  }

  public Long getEventId() {
    return eventId;
  }

  public void eventId(final Long eventId) {
    this.eventId = eventId;
  }

  public Long getCaseId() {
    return caseId;
  }

  public void setCaseId(final Long caseId) {
    this.caseId = caseId;
  }

  public Long getProcessModelId() {
    return processModelId;
  }

  public void setProcessModelId(final Long processModelId) {
    this.processModelId = processModelId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final String timestamp) {
    this.timestamp = timestamp;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(final String activity) {
    this.activity = activity;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(final String resource) {
    this.resource = resource;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(final String messageType) {
    this.messageType = messageType;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }
}
