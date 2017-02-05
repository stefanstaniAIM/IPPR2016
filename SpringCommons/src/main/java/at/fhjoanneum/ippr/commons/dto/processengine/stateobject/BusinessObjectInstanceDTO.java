package at.fhjoanneum.ippr.commons.dto.processengine.stateobject;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

@XmlRootElement
public class BusinessObjectInstanceDTO implements Serializable {

  private static final long serialVersionUID = 1364364841800130590L;

  private Long bomId;

  private List<BusinessObjectFieldInstanceDTO> fields = Lists.newArrayList();

  private List<BusinessObjectInstanceDTO> children = Lists.newArrayList();

  public BusinessObjectInstanceDTO() {}



  public BusinessObjectInstanceDTO(final Long bomId,
      final List<BusinessObjectFieldInstanceDTO> fields,
      final List<BusinessObjectInstanceDTO> children) {
    this.bomId = bomId;
    this.fields = fields;
    this.children = children;
  }

  public Long getBomId() {
    return bomId;
  }

  public List<BusinessObjectFieldInstanceDTO> getFields() {
    return fields;
  }

  public List<BusinessObjectInstanceDTO> getChildren() {
    return children;
  }

  public List<BusinessObjectInstanceDTO> flattened() {
    return getAll().collect(Collectors.toList());
  }

  private Stream<BusinessObjectInstanceDTO> getAll() {
    return Stream.concat(Stream.of(this),
        children.stream().flatMap(BusinessObjectInstanceDTO::getAll));
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("bomId", bomId)
        .append("fields", fields).toString();
  }
}
