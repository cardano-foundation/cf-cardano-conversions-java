package org.cardanofoundation.conversions.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ConversionRuntimeException;

@Data
@ToString
@Slf4j
public class ByronGenesis {

  public static final String ATTR_START_TIME = "startTime";
  public static final String ATTR_BLOCK_VERSION_DATA = "blockVersionData";
  public static final String ATTR_SLOT_DURATION = "slotDuration";
  public static final String ATTR_PROTOCOL_CONSTS = "protocolConsts";
  public static final String ATTR_PROTOCOL_MAGIC = "protocolMagic";

  private final ObjectMapper objectMapper;

  private long startTime;
  private long byronSlotLength;
  private long protocolMagic;

  public ByronGenesis(InputStream is, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    parseByronGenesisFile(is);
  }

  public Duration getByronSlotLength() {
    return Duration.ofSeconds(byronSlotLength);
  }

  private void parseByronGenesisFile(InputStream is) {
    ObjectNode byronJsonNode = parseJson(is);
    startTime = byronJsonNode.get(ATTR_START_TIME).asLong();
    byronSlotLength =
        byronJsonNode.get(ATTR_BLOCK_VERSION_DATA).get(ATTR_SLOT_DURATION).asLong()
            / 1000; // in second

    protocolMagic = byronJsonNode.get(ATTR_PROTOCOL_CONSTS).get(ATTR_PROTOCOL_MAGIC).asLong();

    log.debug("startTime: {}", startTime);
    log.debug("byronSlotLength: {}", byronSlotLength);
    log.debug("protocolMagic: {}", protocolMagic);
  }

  private ObjectNode parseJson(InputStream is) {
    ObjectNode jsonNode;
    try {
      jsonNode = (ObjectNode) objectMapper.readTree(is);
    } catch (IOException e) {
      throw new ConversionRuntimeException("Error parsing byron genesis file", e);
    }

    return jsonNode;
  }
}
