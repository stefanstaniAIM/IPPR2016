package at.fhjoanneum.ippr.communicator.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.fhjoanneum.ippr.communicator.persistence.objects.internal.InternalData;

public final class InternalDataUtils {

  private InternalDataUtils() {}

  public static String convertInternalDataToJson(final InternalData data)
      throws JsonProcessingException {
    final ObjectMapper mapper = new ObjectMapper();
    final String value = mapper.writeValueAsString(data);
    return value;
  }

  public static InternalData convertJsonToInternalData(final String data)
      throws JsonParseException, JsonMappingException, IOException {
    final ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(data, InternalData.class);
  }
}
