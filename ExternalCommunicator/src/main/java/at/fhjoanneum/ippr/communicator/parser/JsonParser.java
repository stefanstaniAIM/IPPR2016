package at.fhjoanneum.ippr.communicator.parser;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import at.fhjoanneum.ippr.communicator.global.GlobalKey;
import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class JsonParser implements Parser {

  private final static Logger LOG = LoggerFactory.getLogger(JsonParser.class);

  private String typeName = "TYPE";

  private final Table<String, String, String> cache = HashBasedTable.create();

  @Override
  public InternalData parse(final String input, final MessageProtocol messageProtocol,
      final Map<DataType, DataTypeParser> parser, final Map<String, String> configuration)
      throws Exception {
    final JSONObject object = new JSONObject(input);

    typeName = configuration.get(GlobalKey.TYPE);

    getValues(object, null);

    LOG.debug("{}", cache);

    return null;
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

  private InternalData convert() {
    return null;
  }

  @Override
  public String getDescription() {
    return "JSON Parser";
  }
}
