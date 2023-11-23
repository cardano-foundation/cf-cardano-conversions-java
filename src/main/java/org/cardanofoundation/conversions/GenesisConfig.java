package org.cardanofoundation.conversions;

import static java.time.ZoneOffset.UTC;
import static org.cardanofoundation.conversions.domain.Era.Byron;
import static org.cardanofoundation.conversions.domain.Era.Shelley;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.domain.ByronGenesis;
import org.cardanofoundation.conversions.domain.Era;
import org.cardanofoundation.conversions.domain.NetworkType;
import org.cardanofoundation.conversions.domain.ShelleyGenesis;

@Slf4j
public class GenesisConfig {

  public static final int PREVIEW_EPOCH_LENGTH = 86400;

  private static final long DEFAULT_EPOCH_LENGTH = 432000; // 5 days

  private final ConversionsConfig conversionsConfig;
  private final ObjectMapper objectMapper;

  @Getter private Duration byronSlotLength;

  @Getter private Duration shelleySlotLength;

  @Getter private double activeSlotsCoeff;

  @Getter private long cardanoNetworkStartTime;

  @Getter private long shelleyEpochLength;

  @Getter private BigInteger maxLovelaceSupply;

  @Getter private long protocolNetworkMagic;

  public GenesisConfig(ConversionsConfig conversionsConfig, ObjectMapper objectMapper) {
    this.conversionsConfig = conversionsConfig;
    this.objectMapper = objectMapper;

    var byronGenesis = parseByronGenesisFile(conversionsConfig.byronGenesisFile());
    var shelleyGenesis = parseShelleyGenesisFile(conversionsConfig.shelleyGenesisFile());

    var distinctProtocolMagics =
        Stream.of(
                byronGenesis.getProtocolMagic(),
                shelleyGenesis.getNetworkMagic(),
                conversionsConfig.networkType().getProtocolMagic())
            .distinct()
            .count();

    if (distinctProtocolMagics != 1) {
      throw new ConversionRuntimeException(
          "Protocol magics mismatch, only one distinct networkProtocolMagic allowed!");
    }
  }

  public LocalDateTime getStartTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(cardanoNetworkStartTime), UTC);
  }

  public LocalDateTime getShelleyStartTime() {
    return getStartTime().plusSeconds(firstShelleySlot() * slotDuration(Byron).getSeconds());
  }

  public LocalDateTime blockTime(Era era, long slot) {
    if (era == Byron) {
      return byronBlockTime(slot);
    }

    var slotsFromShelleyStart = slot - firstShelleySlot();

    return getShelleyStartTime()
        .plusSeconds(slotsFromShelleyStart * slotDuration(Shelley).getSeconds());
  }

  private LocalDateTime byronBlockTime(long slot) {
    return getStartTime().plusSeconds(slot * slotDuration(Byron).getSeconds());
  }

  /**
   * Given era return duration of a slot
   *
   * @param era
   * @return duration of a slot in a given era
   */
  public Duration slotDuration(Era era) {
    if (era == Byron) {
      return byronSlotLength;
    }

    return shelleySlotLength;
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
    return switch (conversionsConfig.networkType()) {
      case MAINNET -> 208;
      case PREPROD -> 4;
      default -> throw new ConversionRuntimeException("Network not yet supported!");
    };
  }

  public long lastByronSlot() {
    return ((lastByronEpochNo() + 1) * slotsPerEpoch(Byron)) - 1;
  }

  public long firstShelleySlot() {
    return lastByronSlot() + 1;
  }

  private ByronGenesis parseByronGenesisFile(URL byronGenesisFile) {
    var byronGenesis = parseByronGenesis(byronGenesisFile);

    cardanoNetworkStartTime = byronGenesis.getStartTime();
    byronSlotLength = byronGenesis.getByronSlotLength(); // in second
    protocolNetworkMagic = byronGenesis.getProtocolMagic();

    log.debug("Cardano network start time: {}", getStartTime());
    log.debug("Byron slot length: {}", byronSlotLength);
    log.debug("Protocol network magic: {}", protocolNetworkMagic);

    return byronGenesis;
  }

  private ShelleyGenesis parseShelleyGenesisFile(URL shelleyGenesisFile) {
    var shelleyGenesis = parseShelleyGenesis(shelleyGenesisFile);
    shelleySlotLength = Duration.ofSeconds((int) shelleyGenesis.getSlotLength());
    activeSlotsCoeff = shelleyGenesis.getActiveSlotsCoeff();
    maxLovelaceSupply = shelleyGenesis.getMaxLovelaceSupply();
    shelleyEpochLength = shelleyGenesis.getEpochLength();
    protocolNetworkMagic = shelleyGenesis.getNetworkMagic();

    log.debug("Shelley slot length: {}", shelleySlotLength);
    log.debug("Active slots coeff: {}", activeSlotsCoeff);

    return shelleyGenesis;
  }

  @SneakyThrows
  private ByronGenesis parseByronGenesis(URL byronGenesisFile) {
    try (InputStream inputStream = byronGenesisFile.openStream()) {
      return new ByronGenesis(inputStream, objectMapper);
    }
  }

  @SneakyThrows
  private ShelleyGenesis parseShelleyGenesis(URL shelleyGenesisFile) {
    try (InputStream inputStream = shelleyGenesisFile.openStream()) {
      return new ShelleyGenesis(inputStream, objectMapper);
    }
  }
}
