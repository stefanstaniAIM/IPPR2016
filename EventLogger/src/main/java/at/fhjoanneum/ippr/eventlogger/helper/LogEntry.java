package at.fhjoanneum.ippr.eventlogger.helper;

public class LogEntry {
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
    private LogKey nextLogEntryKey;

    public LogEntry() {}

    public LogEntry(Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType, String recipient, String sender) {
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

    public LogEntry(Long eventId, Long caseId, Long processModelId, String timestamp, String activity, String resource, String state, String messageType, String recipient, String sender) {
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

    public LogKey getNextLogEntryKey() {
        return nextLogEntryKey;
    }

    public void setNextLogEntryKey(LogKey nextLogEntryKey) {
        this.nextLogEntryKey = nextLogEntryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntry logEntry = (LogEntry) o;

        if (eventId != null ? !eventId.equals(logEntry.eventId) : logEntry.eventId != null) return false;
        if (caseId != null ? !caseId.equals(logEntry.caseId) : logEntry.caseId != null) return false;
        if (processModelId != null ? !processModelId.equals(logEntry.processModelId) : logEntry.processModelId != null)
            return false;
        if (timestamp != null ? !timestamp.equals(logEntry.timestamp) : logEntry.timestamp != null) return false;
        if (activity != null ? !activity.equals(logEntry.activity) : logEntry.activity != null) return false;
        if (resource != null ? !resource.equals(logEntry.resource) : logEntry.resource != null) return false;
        if (state != null ? !state.equals(logEntry.state) : logEntry.state != null) return false;
        if (messageType != null ? !messageType.equals(logEntry.messageType) : logEntry.messageType != null)
            return false;
        if (recipient != null ? !recipient.equals(logEntry.recipient) : logEntry.recipient != null) return false;
        if (sender != null ? !sender.equals(logEntry.sender) : logEntry.sender != null) return false;
        return nextLogEntryKey != null ? nextLogEntryKey.equals(logEntry.nextLogEntryKey) : logEntry.nextLogEntryKey == null;
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
        result = 31 * result + (nextLogEntryKey != null ? nextLogEntryKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
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
                ", nextLogEntryKey=" + nextLogEntryKey +
                '}';
    }
}
