package at.fhjoanneum.ippr.commons.dto.owlimport.reader;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement
public class OWLProcessModelDTO implements Serializable {

  private java.lang.String name;
  private LocalDateTime createdAt;

  private List<OWLSubjectModelDTO> subjectModels;
  private List<OWLStateDTO> states = new ArrayList<>();
  private List<OWLTransitionDTO> transitions = new ArrayList<>();
  private List<OWLBomDTO> boms = new ArrayList<>();
  private List<OWLMessageFlowDTO> messageFlows = new ArrayList<>();

  public OWLProcessModelDTO() {}

  public OWLProcessModelDTO(final java.lang.String name, final LocalDateTime createdAt,
                            final List<OWLSubjectModelDTO> subjectModels, final List<OWLStateDTO> states,
                            final List<OWLTransitionDTO> transitions, final List<OWLBomDTO> boms,
                            final List<OWLMessageFlowDTO> messageFlows) {
    this.name = name;
    this.createdAt = createdAt;
    this.subjectModels = subjectModels;
    this.states = states;
    this.transitions = transitions;
    this.boms = boms;
    this.messageFlows = messageFlows;
  }

  public java.lang.String getName() {
    return name;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public List<OWLSubjectModelDTO> getSubjectModels() {
    return subjectModels;
  }

  public List<OWLStateDTO> getStates() {
    return states;
  }

  public List<OWLTransitionDTO> getTransitions() {
    return transitions;
  }

  public List<OWLBomDTO> getBoms() {
    return boms;
  }

  public List<OWLMessageFlowDTO> getMessageFlows() {
    return messageFlows;
  }
}
