package at.fhjoanneum.ippr.eventlogger.persistence;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "EVENT_LOG")
public class EventLogEntry implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eventId;

  @Column
  private Long caseId;

  @Column
  private Long processModelId;

  @Column
  private String timestamp;

  @Column
  private String activity;

  @Column
  private String resource;

  @Column
  private String state;

  @Column
  private String messageType;

    @Column
    private String recipient;

    @Column
    private String sender;

  public EventLogEntry() {}

  public EventLogEntry(Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType, String recipient, String sender) {
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

    public EventLogEntry(Long eventId, Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType, String recipient, String sender) {
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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Long getEventId() {
    return eventId;
  }

  public Long getCaseId() {
    return caseId;
  }

    public Long getProcessModelId() {
        return processModelId;
    }

    public String getTimestamp() {
    return timestamp;
    }

    public String getActivity() {
    return activity;
    }

    public String getResource() {
    return resource;
    }

    public String getState() {
    return state;
    }

    public String getMessageType() {
    return messageType;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public void setProcessModelId(Long processModelId) {
        this.processModelId = processModelId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMessageType(String messageType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventLogEntry that = (EventLogEntry) o;

        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (caseId != null ? !caseId.equals(that.caseId) : that.caseId != null) return false;
        if (processModelId != null ? !processModelId.equals(that.processModelId) : that.processModelId != null)
            return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (activity != null ? !activity.equals(that.activity) : that.activity != null) return false;
        if (resource != null ? !resource.equals(that.resource) : that.resource != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (messageType != null ? !messageType.equals(that.messageType) : that.messageType != null) return false;
        if (recipient != null ? !recipient.equals(that.recipient) : that.recipient != null) return false;
        return sender != null ? sender.equals(that.sender) : that.sender == null;
    }

    @Override
    public int hashCode() {
        int result = eventId != null ? eventId.hashCode() : 0;
        result = 31 * result + (caseId != null ? caseId.hashCode() : 0);
        result = 31 * result + (processModelId != null ? processModelId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (activity != null ? activity.hashCode() : 0);
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventLogEntry{" +
                "eventId=" + eventId +
                ", caseId=" + caseId +
                ", processModelId=" + processModelId +
                ", timestamp='" + timestamp + '\'' +
                ", activity='" + activity + '\'' +
                ", resource='" + resource + '\'' +
                ", state='" + state + '\'' +
                ", messageType='" + messageType + '\'' +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
