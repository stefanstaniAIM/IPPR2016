package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLTransitionDTO implements Serializable {

    private String toStateId;
    private String fromStateId;

    public OWLTransitionDTO() {}

    public OWLTransitionDTO(String fromStateId, String toStateId) {
        this.fromStateId = fromStateId;
        this.toStateId = toStateId;
    }

    public String getFromStateId() {
        return fromStateId;
    }

    public String getToStateId() {
        return toStateId;
    }

}
