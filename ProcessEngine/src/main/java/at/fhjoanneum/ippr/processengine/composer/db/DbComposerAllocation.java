package at.fhjoanneum.ippr.processengine.composer.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;

@Configuration
public class DbComposerAllocation {

  @Autowired
  private DbDateComposer dbDateComposer;
  @Autowired
  private DbDecimalComposer dbDecimalComposer;
  @Autowired
  private DbNumberComposer dbNumberComposer;
  @Autowired
  private DbStringComposer dbStringComposer;
  @Autowired
  private DbTimestampComposer dbTimestampComposer;

  public <T> DbComposer<T> getComposer(final FieldType fieldType) {
    switch (fieldType) {
      case DATE:
        return (DbComposer<T>) dbDateComposer;
      case DECIMAL:
        return (DbComposer<T>) dbDecimalComposer;
      case NUMBER:
        return (DbComposer<T>) dbNumberComposer;
      case STRING:
        return (DbComposer<T>) dbStringComposer;
      case TIMESTAMP:
        return (DbComposer<T>) dbTimestampComposer;
      default:
        throw new IllegalArgumentException("Could not find db composer");
    }
  }
}
