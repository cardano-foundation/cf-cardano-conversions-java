package org.cardanofoundation.conversions.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import org.cardanofoundation.conversions.ConversionRuntimeException;

public class MoreJson {

  public static ObjectNode parseJson(ObjectMapper objectMapper, InputStream is) {
    ObjectNode jsonNode;
    try {
      jsonNode = (ObjectNode) objectMapper.readTree(is);
    } catch (IOException e) {
      throw new ConversionRuntimeException("Error parsing shelley genesis file", e);
    }

    return jsonNode;
  }
}
