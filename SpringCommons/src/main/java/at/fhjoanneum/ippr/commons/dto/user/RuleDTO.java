package at.fhjoanneum.ippr.commons.dto.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RuleDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long ruleId;
  private String ruleName;

  public RuleDTO() {}

  public RuleDTO(final Long ruleId, final String ruleName) {
    this.ruleId = ruleId;
    this.ruleName = ruleName;
  }

  public Long getRuleId() {
    return ruleId;
  }

  public String getRuleName() {
    return ruleName;
  }
}
