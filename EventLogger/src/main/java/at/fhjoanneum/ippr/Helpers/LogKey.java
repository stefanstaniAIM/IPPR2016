package at.fhjoanneum.ippr.Helpers;

public class LogKey {

    private String activity;
    private String state;
    private String messageType;

    public LogKey(String activity, String state, String messageType) {
        this.activity = activity;
        this.state = state;
        this.messageType = messageType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogKey logKey = (LogKey) o;

        if (activity != null ? !activity.equals(logKey.activity) : logKey.activity != null) return false;
        if (state != null ? !state.equals(logKey.state) : logKey.state != null) return false;
        return messageType != null ? messageType.equals(logKey.messageType) : logKey.messageType == null;
    }

    @Override
    public int hashCode() {
        int result = activity != null ? activity.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        return result;
    }
}
