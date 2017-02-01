package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLTransitionDTO implements Serializable {

    private OWLStateDTO toState;

    public OWLTransitionDTO() {}

    public OWLTransitionDTO(OWLStateDTO fromState, OWLStateDTO toState) {
        this.fromState = fromState;
        this.toState = toState;
    }

    private OWLStateDTO fromState;

    public OWLStateDTO getFromState() {
        return fromState;
    }

    public OWLStateDTO getToState() {
        return toState;
    }

}
