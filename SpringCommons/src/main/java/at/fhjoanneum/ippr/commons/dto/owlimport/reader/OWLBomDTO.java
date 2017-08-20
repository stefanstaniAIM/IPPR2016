package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;


@XmlRootElement
public class OWLBomDTO implements Serializable {

    private String name;
    private HashSet<String> stateIds = new HashSet<>();
    private String id;

    public OWLBomDTO() {}

    public OWLBomDTO(final String id, final String name, final HashSet<String> stateIds) {

        this.name = name;
        this.stateIds = stateIds;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public HashSet<String> getStateIds() { return stateIds; }

    public String getId() { return id; }
}
