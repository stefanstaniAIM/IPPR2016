package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLMessageFlowDTO implements Serializable {

    private String senderId;
    private String receiverId;
    private String stateId;
    private String bomId;
    private String id;

    public OWLMessageFlowDTO() {}

    public OWLMessageFlowDTO(String id, String sender, String receiver, String stateId, String bomId) {
        this.id = id;
        this.senderId = sender;
        this.receiverId = receiver;
        this.stateId = stateId;
        this.bomId = bomId;
    }

    public String getId() { return id; }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getStateId() {
        return stateId;
    }

    public String getBomId() {
        return bomId;
    }
}
