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
  private String to;
  private String from;

  public EventLoggerDTO() {}

  public EventLoggerDTO(final Long caseId, final Long processModelId, final String timestamp,
      final String activity, final String resource, final String state, final String messageType, final String to, final String from) {
    this.caseId = caseId;
    this.processModelId = processModelId;
    this.timestamp = timestamp;
    this.activity = activity;
    this.resource = resource;
    this.state = state;
    this.messageType = messageType;
    this.to = to;
    this.from = from;
  }

  public EventLoggerDTO(final Long eventId, final Long caseId, final Long processModelId,
      final String timestamp, final String activity, final String resource, final String state,
      final String messageType, final String to, final String from) {
    this.eventId = eventId;
    this.caseId = caseId;
    this.processModelId = processModelId;
    this.timestamp = timestamp;
    this.activity = activity;
    this.resource = resource;
    this.state = state;
    this.messageType = messageType;
    this.to = to;
    this.from = from;
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

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }
}
