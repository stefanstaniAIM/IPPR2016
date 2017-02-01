package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement
public class BusinessObjectDTO implements Serializable {

  private static final long serialVersionUID = -8170255134093311778L;

  private Long bomId;
  private Long boiId;
  private String name;
  private List<BusinessObjectFieldDTO> fields = Lists.newArrayList();
  private List<BusinessObjectDTO> children = Lists.newArrayList();

  public BusinessObjectDTO() {}


  public BusinessObjectDTO(final Long bomId, final Long boiId, final String name,
      final List<BusinessObjectFieldDTO> businessObjectFields) {
    this.bomId = bomId;
    this.boiId = boiId;
    this.name = name;
    this.fields = businessObjectFields;
  }

  public BusinessObjectDTO(final Long bomId, final Long boiId, final String name,
      final List<BusinessObjectFieldDTO> businessObjectFields,
      final List<BusinessObjectDTO> children) {
    this(bomId, boiId, name, businessObjectFields);
    this.children = children;
  }

  public Long getBomId() {
    return bomId;
  }

  public Long getBoiId() {
    return boiId;
  }

  public String getName() {
    return name;
  }

  public List<BusinessObjectFieldDTO> getFields() {
    return fields;
  }

  public List<BusinessObjectDTO> getChildren() {
    return children;
  }

  public boolean hasNonEmptyFields() {
    return getAll().filter(bo -> {
      return bo.getFields() != null && !bo.getFields().isEmpty();
    }).count() >= 1;
  }

  private Stream<BusinessObjectDTO> getAll() {
    return Stream.concat(Stream.of(this), children.stream().flatMap(BusinessObjectDTO::getAll));
  }
}
