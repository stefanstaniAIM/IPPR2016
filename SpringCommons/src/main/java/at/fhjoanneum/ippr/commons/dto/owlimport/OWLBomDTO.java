package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
public class OWLBomDTO implements Serializable {

    private String name;
    private List<OWLStateDTO> states = new ArrayList<>();

    public OWLBomDTO() {}

    public OWLBomDTO(final String name, final List<OWLStateDTO> states) {

        this.name = name;
        this.states = states;
    }

    public String getName() {
        return name;
    }

    public List<OWLStateDTO> getStates() { return states; }
}
