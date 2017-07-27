package at.fhjoanneum.ippr.eventlogger.helper;

public class LogKey {

    private String activity;
    private String state;
    private String messageType;
    private String to;
    private String from;

    public LogKey(String activity, String state, String messageType, String to, String from) {
        this.activity = activity;
        this.state = state;
        this.messageType = messageType;
        this.to = to;
        this.from = from;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogKey logKey = (LogKey) o;

        if (activity != null ? !activity.equals(logKey.activity) : logKey.activity != null) return false;
        if (state != null ? !state.equals(logKey.state) : logKey.state != null) return false;
        if (messageType != null ? !messageType.equals(logKey.messageType) : logKey.messageType != null) return false;
        if (to != null ? !to.equals(logKey.to) : logKey.to != null) return false;
        return from != null ? from.equals(logKey.from) : logKey.from == null;
    }

    @Override
    public int hashCode() {
        int result = activity != null ? activity.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        return result;
    }
}
