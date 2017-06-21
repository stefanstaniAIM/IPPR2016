package at.fhjoanneum.ippr.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "EVENT_LOG")
public class EventLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long eId;

  @Column
  private Long caseId;

  public EventLog() {}

  public EventLog(final Long caseId) {
    this.caseId = caseId;
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


  @Override
  public String toString() {
    return "EventLog [eId=" + eId + ", caseId=" + caseId + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((caseId == null) ? 0 : caseId.hashCode());
    result = prime * result + ((eId == null) ? 0 : eId.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final EventLog other = (EventLog) obj;
    if (caseId == null) {
      if (other.caseId != null) {
        return false;
      }
    } else if (!caseId.equals(other.caseId)) {
      return false;
    }
    if (eId == null) {
      if (other.eId != null) {
        return false;
      }
    } else if (!eId.equals(other.eId)) {
      return false;
    }
    return true;
  }
}
