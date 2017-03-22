package at.fhjoanneum.ippr.communicator.parser;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import at.fhjoanneum.ippr.communicator.persistence.objects.DataType;
import at.fhjoanneum.ippr.communicator.persistence.objects.datatypeparser.DataTypeParser;
import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;
import at.fhjoanneum.ippr.communicator.persistence.objects.protocol.MessageProtocol;

public class JsonParser implements Parser {

  @Override
  public InternalData parse(final String input, final MessageProtocol messageProtocol,
      final Map<DataType, DataTypeParser> parser) throws Exception {
    final JSONObject object = new JSONObject(input);

    getValues(object, null);

    return null;
  }

  private void getValues(final JSONObject object, final String type) throws JSONException {
    final Iterator<String> keys = object.keys();
    while (keys.hasNext()) {
      final String key = keys.next();
      try {
        final JSONObject child = object.getJSONObject(key);
        getValues(child, key);
      } catch (final JSONException e) {
        final String value = object.getString(key);
      }
    }
  }

  @Override
  public String getDescription() {
    return "JSON Parser";
  }
}
