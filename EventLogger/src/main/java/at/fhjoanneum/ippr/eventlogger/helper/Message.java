package at.fhjoanneum.ippr.eventlogger.helper;

/**
 * Created by Matthias on 11.08.2017.
 */
public class Message {

    private String name;
    private String recipient;
    private String sender;
    private String messagePlaceId;

    public Message(String name, String recipient, String sender, String messagePlaceId) {
        this.name = name;
        this.recipient = recipient;
        this.sender = sender;
        this.messagePlaceId = messagePlaceId;
    }

    public Message(String name, String recipient, String sender) {
        this.name = name;
        this.recipient = recipient;
        this.sender = sender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMessagePlaceId() {
        return messagePlaceId;
    }

    public void setMessagePlaceId(String messagePlaceId) {
        this.messagePlaceId = messagePlaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (name != null ? !name.equals(message.name) : message.name != null) return false;
        if (recipient != null ? !recipient.equals(message.recipient) : message.recipient != null) return false;
        if (sender != null ? !sender.equals(message.sender) : message.sender != null) return false;
        return messagePlaceId != null ? messagePlaceId.equals(message.messagePlaceId) : message.messagePlaceId == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (messagePlaceId != null ? messagePlaceId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                ", messagePlaceId='" + messagePlaceId + '\'' +
                '}';
    }
}
