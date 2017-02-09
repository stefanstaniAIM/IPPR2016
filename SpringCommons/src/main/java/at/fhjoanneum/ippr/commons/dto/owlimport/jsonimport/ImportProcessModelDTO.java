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

  private List<ImportSubjectModelDTO> subjectModels;
  private List<ImportStateDTO> states;
  private List<ImportTransitionDTO> transitions;
  private List<ImportBusinessObjectModelDTO> boms;
  private List<ImportBusinessObjectFieldsModelDTO> bofms;
  private List<ImportBusinessObjectFieldPermissionDTO> bofps;
  private List<ImportMessageFlowDTO> messageFlows;

  public ImportProcessModelDTO() {}

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
}
