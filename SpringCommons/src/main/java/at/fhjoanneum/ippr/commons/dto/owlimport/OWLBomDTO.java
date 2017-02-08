package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
public class OWLBomDTO implements Serializable {

    private String name;
    private List<String> stateIds = new ArrayList<>();
    private String id;

    public OWLBomDTO() {}

    public OWLBomDTO(final String id, final String name, final List<String> stateIds) {

        this.name = name;
        this.stateIds = stateIds;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<String> getStateIds() { return stateIds; }

    public String getId() { return id; }
}
