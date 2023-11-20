package org.cardanofoundation.conversions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.cardanofoundation.ConversionsConfig;
import org.cardanofoundation.conversions.domain.ByronGenesis;
import org.cardanofoundation.conversions.domain.Era;
import org.cardanofoundation.conversions.domain.NetworkType;
import org.cardanofoundation.conversions.domain.ShelleyGenesis;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;

import static org.cardanofoundation.conversions.domain.Era.Byron;

public class GenesisConfig {

  public static final int PREVIEW_EPOCH_LENGTH = 86400;

  private static final long DEFAULT_EPOCH_LENGTH = 432000; // 5 days

  private final ObjectMapper objectMapper;

  @Getter private String shelleyStartTime;

  @Getter private long byronSlotLength;

  @Getter private double shelleySlotLength;

  @Getter private double activeSlotsCoeff;

  @Getter private long cardanoNetworkStartTime;

  @Getter private long shelleyEpochLength;

  @Getter private BigInteger maxLovelaceSupply;

  @Getter private long protocolNetworkMagic;

  public GenesisConfig(ConversionsConfig conversionsConfig,
                       ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;

    parseByronGenesisFile(conversionsConfig.byronGenesisFile());
    parseShelleyGenesisFile(conversionsConfig.shelleyGenesisFile());
  }

  /**
   * Given era return duration of a slot
   *
   * @param era
   * @return duration of a slot in a given era
   */
  public Duration slotDuration(Era era) {
    if (era == Byron) {
      if (byronSlotLength == 0) {
        return Duration.ofSeconds(20);
      }

      return Duration.ofSeconds(byronSlotLength);
    }

    if (shelleySlotLength == 0) {
      return Duration.ofSeconds(1);
    }

    return Duration.ofSeconds(Double.valueOf(shelleySlotLength).longValue());
  }

  public long slotsPerEpoch(Era era) {
    return (getEpochLengthInSlots() / slotDuration(era).getSeconds());
  }

  public long getEpochLengthInSlots() {
    var networkTypeM = NetworkType.fromProtocolMagic(protocolNetworkMagic);
    if (networkTypeM.isEmpty()) {
      throw new ConversionRuntimeException("Unknown protocol magic: " + protocolNetworkMagic);
    }

    var networkType = networkTypeM.get();

    return getEpochLengthInSlots(networkType);
  }

  public long getEpochLengthInSlots(NetworkType networkType) {
    return switch (networkType) {
      case PREVIEW -> PREVIEW_EPOCH_LENGTH;
      default -> DEFAULT_EPOCH_LENGTH;
    };
  }

  public int lastByronEpochNo() {
    return firstShelleyEpochNo() - 1;
  }

  public int firstShelleyEpochNo() {
    // TODO make it work for all network types
    return 208;
  }

  public long lastByronSlot() {
    return ((lastByronEpochNo() + 1) * slotsPerEpoch(Byron)) - 1;
  }

  public long firstShelleySlot() {
    return lastByronSlot() + 1;
  }

  private void parseByronGenesisFile(URL byronGenesisFile) {
    var byronGenesis = getByronGenesis(byronGenesisFile);

    cardanoNetworkStartTime = byronGenesis.getStartTime();
    byronSlotLength = byronGenesis.getByronSlotLength(); // in second
    protocolNetworkMagic = byronGenesis.getProtocolMagic();
  }

  private void parseShelleyGenesisFile(URL shelleyGenesisFile) {
    var shelleyGenesis = getShelleyGenesis(shelleyGenesisFile);

    shelleyStartTime = shelleyGenesis.getSystemStart();
    shelleySlotLength = shelleyGenesis.getSlotLength();
    activeSlotsCoeff = shelleyGenesis.getActiveSlotsCoeff();
    maxLovelaceSupply = shelleyGenesis.getMaxLovelaceSupply();
    shelleyEpochLength = shelleyGenesis.getEpochLength();
    protocolNetworkMagic = shelleyGenesis.getNetworkMagic();
  }

  @SneakyThrows
  private ByronGenesis getByronGenesis(URL byronGenesisFile) {
    try (InputStream inputStream = byronGenesisFile.openStream()) {
      return new ByronGenesis(inputStream, objectMapper);
    }
  }

  @SneakyThrows
  private ShelleyGenesis getShelleyGenesis(URL shelleyGenesisFile) {
    try (InputStream inputStream = shelleyGenesisFile.openStream()) {
      return new ShelleyGenesis(inputStream, objectMapper);
    }
  }
}
