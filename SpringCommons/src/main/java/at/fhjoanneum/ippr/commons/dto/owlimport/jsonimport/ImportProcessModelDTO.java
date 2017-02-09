package at.fhjoanneum.ippr.commons.dto.owlimport.jsonimport;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ImportProcessModelDTO implements Serializable {

  private static final long serialVersionUID = -3950877010952093278L;

  private String name;
  private String description;
  private String startSubjectModelId;
  private float version;

  private List<ImportSubjectModelDTO> subjectModels;
  private List<ImportStateDTO> states;
  private List<ImportTransitionDTO> transitions;
  private List<ImportBusinessObjectModelDTO> boms;
  private List<ImportBusinessObjectFieldsModelDTO> bofms;
  private List<ImportBusinessObjectFieldPermissionDTO> bofps;
  private List<ImportMessageFlowDTO> messageFlows;

  public ImportProcessModelDTO() {}



  public ImportProcessModelDTO(final String name, final String description,
      final String startSubjectModelId, final List<ImportSubjectModelDTO> subjectModels,
      final List<ImportStateDTO> states, final List<ImportTransitionDTO> transitions,
      final List<ImportBusinessObjectModelDTO> boms,
      final List<ImportBusinessObjectFieldsModelDTO> bofms,
      final List<ImportBusinessObjectFieldPermissionDTO> bofps,
      final List<ImportMessageFlowDTO> messageFlows, final float version) {
    this.name = name;
    this.description = description;
    this.startSubjectModelId = startSubjectModelId;
    this.subjectModels = subjectModels;
    this.states = states;
    this.transitions = transitions;
    this.boms = boms;
    this.bofms = bofms;
    this.bofps = bofps;
    this.messageFlows = messageFlows;
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getStartSubjectModelId() {
    return startSubjectModelId;
  }

  public List<ImportSubjectModelDTO> getSubjectModels() {
    return subjectModels;
  }

  public List<ImportStateDTO> getStates() {
    return states;
  }

  public List<ImportTransitionDTO> getTransitions() {
    return transitions;
  }

  public List<ImportBusinessObjectModelDTO> getBoms() {
    return boms;
  }

  public List<ImportBusinessObjectFieldsModelDTO> getBofms() {
    return bofms;
  }

  public List<ImportBusinessObjectFieldPermissionDTO> getBofps() {
    return bofps;
  }

  public List<ImportMessageFlowDTO> getMessageFlows() {
    return messageFlows;
  }

  public float getVersion() {
    return version;
  }
}
