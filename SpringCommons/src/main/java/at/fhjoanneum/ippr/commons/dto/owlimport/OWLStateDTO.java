package at.fhjoanneum.ippr.commons.dto.owlimport;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
public class OWLStateDTO implements Serializable {

    private String name;
    private String subjectModelId;
    private String functionType;
    private String eventType;

    public OWLStateDTO() {}

    public OWLStateDTO(final String name, final String subjectModelId, final String functionType, final String eventType) {

        this.name = name;
        this.subjectModelId = subjectModelId;
        this.functionType = functionType;
        this.eventType = eventType;
    }

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
