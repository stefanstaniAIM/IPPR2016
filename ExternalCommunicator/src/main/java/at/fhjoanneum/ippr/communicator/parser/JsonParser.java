package at.fhjoanneum.ippr.communicator.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import at.fhjoanneum.ippr.communicator.global.GlobalKey;
import at.fhjoanneum.ippr.communicator.parser.datatype.ParserUtils;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalField;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalObject;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class JsonParser implements Parser {

  private final static Logger LOG = LoggerFactory.getLogger(JsonParser.class);

  private String typeName = "TYPE";

  private final Table<String, String, String> cache = HashBasedTable.create();
  private final Map<String, InternalObject> objects = new HashMap<>();

  @Override
  public InternalData parse(final String input, final MessageProtocol messageProtocol,
      final Map<DataType, DataTypeParser> parser, final Map<String, String> configuration)
      throws Exception {
    final JSONObject object = new JSONObject(input);

    typeName = configuration.get(GlobalKey.TYPE);

    getValues(object, null);
    convertToInternalObject(messageProtocol);

    return new InternalData(objects);
  }

  private void getValues(final JSONObject object, String type) throws JSONException {
    if (type == null) {
      type = object.getString(typeName);
    }

    final Iterator<String> keys = object.keys();
    while (keys.hasNext()) {
      final String key = keys.next();
      try {
        final JSONObject child = object.getJSONObject(key);
        getValues(child, key);
      } catch (final JSONException e) {
        if (!key.equals(typeName)) {
          final String value = object.getString(key);
          cache.put(type, key, value);
        }
      }
    }
  }

  private void convertToInternalObject(final MessageProtocol protocol) {
    final Map<String, String> row = cache.row(protocol.getExternalName());
    final Map<String, InternalField> fields = new HashMap<>();

    protocol.getFields().stream().forEachOrdered(field -> {

      if (field.isMandatory()
          && (row == null || StringUtils.isBlank(row.get(field.getExternalName())))) {
        throw new IllegalArgumentException("Missing field [" + field + "]");
      }

      String value = row.get(field.getExternalName());
      value = StringUtils.isBlank(value) ? field.getDefaultValue() : value;
      value = ParserUtils.parse(value, field.getDataType());

      fields.put(field.getInternalName(),
          new InternalField(field.getInternalName(), field.getDataType(), value));
    });

    objects.put(protocol.getInternalName(), new InternalObject(protocol.getInternalName(), fields));

    protocol.getChildren().forEach(this::convertToInternalObject);
  }


  @Override
  public String getDescription() {
    return "JSON Parser";
  }
}
