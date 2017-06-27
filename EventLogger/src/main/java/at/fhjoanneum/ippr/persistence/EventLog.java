package at.fhjoanneum.ippr.persistence;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "EVENT_LOG")
public class EventLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eId;

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

  public EventLog() {}

  public EventLog(Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType) {
    this.caseId = caseId;
    this.processModelId = processModelId;
    this.timestamp = timestamp;
    this.activity = activity;
    this.resource = resource;
    this.state = state;
    this.messageType = messageType;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Long getEId() {
    return eId;
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

    @Override
    public String toString() {
        return "EventLog{" +
                "eId=" + eId +
                ", caseId=" + caseId +
                ", processModelId=" + processModelId +
                ", timestamp='" + timestamp + '\'' +
                ", activity='" + activity + '\'' +
                ", resource='" + resource + '\'' +
                ", state='" + state + '\'' +
                ", messageType='" + messageType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventLog eventLog = (EventLog) o;

        if (eId != null ? !eId.equals(eventLog.eId) : eventLog.eId != null) return false;
        if (caseId != null ? !caseId.equals(eventLog.caseId) : eventLog.caseId != null) return false;
        if (processModelId != null ? !processModelId.equals(eventLog.processModelId) : eventLog.processModelId != null)
            return false;
        if (timestamp != null ? !timestamp.equals(eventLog.timestamp) : eventLog.timestamp != null) return false;
        if (activity != null ? !activity.equals(eventLog.activity) : eventLog.activity != null) return false;
        if (resource != null ? !resource.equals(eventLog.resource) : eventLog.resource != null) return false;
        if (state != null ? !state.equals(eventLog.state) : eventLog.state != null) return false;
        return messageType != null ? messageType.equals(eventLog.messageType) : eventLog.messageType == null;
    }

    @Override
    public int hashCode() {
        int result = eId != null ? eId.hashCode() : 0;
        result = 31 * result + (caseId != null ? caseId.hashCode() : 0);
        result = 31 * result + (processModelId != null ? processModelId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (activity != null ? activity.hashCode() : 0);
        result = 31 * result + (resource != null ? resource.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        return result;
    }
}
