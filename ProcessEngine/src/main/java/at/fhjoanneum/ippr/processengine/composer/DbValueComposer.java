package at.fhjoanneum.ippr.processengine.composer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.fhjoanneum.ippr.persistence.objects.model.enums.FieldType;
import at.fhjoanneum.ippr.processengine.composer.db.DbComposer;
import at.fhjoanneum.ippr.processengine.composer.db.DbComposerAllocation;
import at.fhjoanneum.ippr.processengine.composer.json.JsonComposer;
import at.fhjoanneum.ippr.processengine.composer.json.JsonComposerAllocation;

@Component
public class DbValueComposer {

  @Autowired
  private DbComposerAllocation dbComposserAllocation;

  @Autowired
  private JsonComposerAllocation jsonComposerAllocation;

  public String compose(final String value, final FieldType fieldType) {
    if (StringUtils.isBlank(value)) {
      return StringUtils.EMPTY;
    }

    switch (fieldType) {
      case DATE:
        final DbComposer<LocalDate> dbDateComposer = dbComposserAllocation.getComposer(fieldType);
        final JsonComposer<LocalDate> jsonDateComposer =
            jsonComposerAllocation.getComposer(fieldType);
        return jsonDateComposer.compose(dbDateComposer.compose(value));
      case DECIMAL:
        final DbComposer<Float> dbFloatComposer = dbComposserAllocation.getComposer(fieldType);
        final JsonComposer<Float> jsonFloatComposer = jsonComposerAllocation.getComposer(fieldType);
        return jsonFloatComposer.compose(dbFloatComposer.compose(value));
      case NUMBER:
        final DbComposer<Integer> dbNumberComposer = dbComposserAllocation.getComposer(fieldType);
        final JsonComposer<Integer> jsonNumberComposer =
            jsonComposerAllocation.getComposer(fieldType);
        return jsonNumberComposer.compose(dbNumberComposer.compose(value));
      case STRING:
        final DbComposer<String> dbStringComposer = dbComposserAllocation.getComposer(fieldType);
        final JsonComposer<String> jsonStringComposer =
            jsonComposerAllocation.getComposer(fieldType);
        return jsonStringComposer.compose(dbStringComposer.compose(value));
      case TIMESTAMP:
        final DbComposer<LocalDateTime> dbTimestampComposer =
            dbComposserAllocation.getComposer(fieldType);
        final JsonComposer<LocalDateTime> jsonTimestampComposer =
            jsonComposerAllocation.getComposer(fieldType);
        return jsonTimestampComposer.compose(dbTimestampComposer.compose(value));
      default:
        throw new IllegalArgumentException("Could not find composer");
    }
  }

  public boolean canCompose(final String value, final FieldType fieldType) {
    return dbComposserAllocation.getComposer(fieldType).canCompose(value);
  }

}
