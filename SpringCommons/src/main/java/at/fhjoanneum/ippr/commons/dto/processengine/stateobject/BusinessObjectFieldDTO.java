package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BusinessObjectFieldDTO implements Serializable {

  private static final long serialVersionUID = -2396191843338173896L;

  private Long bofmId;
  private Long bofiId;
  private String name;
  private String type;
  private boolean required;
  private boolean readonly;
  private String value;
  private int indent;

  public BusinessObjectFieldDTO() {}

  public BusinessObjectFieldDTO(final Long bofmId, final Long bofiId, final String name,
      final String type, final boolean required, final boolean readonly, final String value,
      final int indent) {
    this.bofmId = bofmId;
    this.bofiId = bofiId;
    this.name = name;
    this.type = type;
    this.required = required;
    this.readonly = readonly;
    this.value = value;
    this.indent = indent;
  }


  public Long getBofmId() {
    return bofmId;
  }

  public Long getBofiId() {
    return bofiId;
  }

  public boolean isReadonly() {
    return readonly;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public boolean isRequired() {
    return required;
  }

  public String getValue() {
    return value;
  }

  public int getIndent() {
    return indent;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
