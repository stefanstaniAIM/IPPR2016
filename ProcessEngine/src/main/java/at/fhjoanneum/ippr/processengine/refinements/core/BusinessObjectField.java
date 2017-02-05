package at.fhjoanneum.ippr.processengine.refinements.core;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

public class BusinessObjectField {
  private final Long bofmId;
  private final String fieldName;
  private final FieldType fieldType;
  private String value;

  public BusinessObjectField(final Long bofmId, final String fieldName, final String fieldType,
      final String value) {
    this.bofmId = bofmId;
    this.fieldName = fieldName;
    this.fieldType = FieldType.valueOf(fieldType);
    this.value = value;
  }

  public Long getBofmId() {
    return bofmId;
  }

  public String getFieldName() {
    return fieldName;
  }

  public FieldType getFieldType() {
    return fieldType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
