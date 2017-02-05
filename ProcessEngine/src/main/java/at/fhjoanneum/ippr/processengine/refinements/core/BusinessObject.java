package at.fhjoanneum.ippr.processengine.refinements.core;

import java.util.Map;

public class BusinessObject {
  private final Long bomId;
  private final String name;
  private final Map<String, BusinessObjectField> fields;
  private final Map<String, BusinessObject> children;

  public BusinessObject(final Long bomId, final String name,
      final Map<String, BusinessObjectField> fields, final Map<String, BusinessObject> children) {
    this.bomId = bomId;
    this.name = name;
    this.fields = fields;
    this.children = children;
  }

  public Long getBomId() {
    return bomId;
  }

  public String getName() {
    return name;
  }

  public Map<String, BusinessObjectField> getFields() {
    return fields;
  }

  public Map<String, BusinessObject> getChildren() {
    return children;
  }


}
