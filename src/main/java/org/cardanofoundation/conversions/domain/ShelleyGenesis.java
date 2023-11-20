package org.cardanofoundation.conversions.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.cardanofoundation.conversions.ConversionRuntimeException;

@Data
@ToString
public class ShelleyGenesis {

  public static final String ATTR_SYSTEM_START = "systemStart";
  public static final String ATTR_SLOT_LENGTH = "slotLength";
  public static final String ATTR_ACTIVE_SLOTS_COEFF = "activeSlotsCoeff";
  public static final String ATTR_MAX_LOVELACE_SUPPLY = "maxLovelaceSupply";
  public static final String ATTR_EPOCH_LENGTH = "epochLength";
  public static final String ATTR_NETWORK_MAGIC = "networkMagic";

  private final ObjectMapper objectMapper;

  private String systemStart;
  private double slotLength;
  private double activeSlotsCoeff;
  private BigInteger maxLovelaceSupply;
  private long epochLength;
  private long networkMagic;

  private List<GenesisBalance> initialFunds = new ArrayList<>();

  public ShelleyGenesis(InputStream is, ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    parseShelleyGenesisFile(is);
  }

  private void parseShelleyGenesisFile(InputStream is) {
    JsonNode shelleyJsonNode = parseJson(is);

    systemStart = shelleyJsonNode.get(ATTR_SYSTEM_START).asText();
    slotLength = shelleyJsonNode.get(ATTR_SLOT_LENGTH).asDouble();
    activeSlotsCoeff = shelleyJsonNode.get(ATTR_ACTIVE_SLOTS_COEFF).asDouble();
    maxLovelaceSupply = new BigInteger(shelleyJsonNode.get(ATTR_MAX_LOVELACE_SUPPLY).asText());
    epochLength = shelleyJsonNode.get(ATTR_EPOCH_LENGTH).asLong();
    networkMagic = shelleyJsonNode.get(ATTR_NETWORK_MAGIC).asInt();
  }

  private ObjectNode parseJson(InputStream is) {
    ObjectNode jsonNode;
    try {
      jsonNode = (ObjectNode) objectMapper.readTree(is);
    } catch (IOException e) {
      throw new ConversionRuntimeException("Error parsing shelley genesis file", e);
    }

    return jsonNode;
  }
}
