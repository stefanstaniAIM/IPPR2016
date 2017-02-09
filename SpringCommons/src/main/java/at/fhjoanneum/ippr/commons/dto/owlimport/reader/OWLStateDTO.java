package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLStateDTO implements Serializable {

    private String id;
    private String name;
    private String subjectModelId;
    private String functionType;
    private String eventType;

    public OWLStateDTO() {}

    public OWLStateDTO(final String id, final String name, final String subjectModelId, final String functionType, final String eventType) {

        this.id = id;
        this.name = name;
        this.subjectModelId = subjectModelId;
        this.functionType = functionType;
        this.eventType = eventType;
    }

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public String getSubjectModelId() {
        return subjectModelId;
    }

    public String getFunctionType() {
        return functionType;
    }

    public String getEventType() {
        return eventType;
    }
}
