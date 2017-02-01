package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLMessageFlowDTO implements Serializable {

    private OWLSubjectModelDTO sender;
    private OWLSubjectModelDTO receiver;
    private OWLStateDTO state;
    private OWLBomDTO bom;

    public OWLMessageFlowDTO() {}

    public OWLMessageFlowDTO(OWLSubjectModelDTO sender, OWLSubjectModelDTO receiver, OWLStateDTO state, OWLBomDTO bom) {
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
        this.bom = bom;
    }

    public OWLSubjectModelDTO getSender() {
        return sender;
    }

    public OWLSubjectModelDTO getReceiver() {
        return receiver;
    }

    public OWLStateDTO getState() {
        return state;
    }

    public OWLBomDTO getBom() {
        return bom;
    }
}
