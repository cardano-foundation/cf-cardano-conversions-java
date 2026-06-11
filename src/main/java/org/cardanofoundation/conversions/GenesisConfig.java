package org.cardanofoundation.conversions;

import static java.time.ZoneOffset.UTC;
import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;

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
import org.cardanofoundation.conversions.domain.EraHistoryItem;
import org.cardanofoundation.conversions.domain.EraType;
import org.cardanofoundation.conversions.domain.ShelleyGenesis;

@Slf4j
public class GenesisConfig {

  private final ConversionsConfig conversionsConfig;

  private final ObjectMapper objectMapper;

  @Getter private Duration byronSlotLength;

  @Getter private Duration shelleySlotLength;

  @Getter private double activeSlotsCoeff;

  @Getter private long cardanoNetworkStartTime;

  @Getter private long shelleyEpochLength;

  /** Byron epoch length in slots, i.e. 10 * k (security parameter) per the Byron specification. */
  @Getter private long byronEpochLength;

  @Getter private BigInteger maxLovelaceSupply;

  @Getter private long protocolNetworkMagic;

  @Getter private EraHistory eraHistory;

  public GenesisConfig(
      ConversionsConfig conversionsConfig, EraHistory eraHistory, ObjectMapper objectMapper) {
    this.conversionsConfig = conversionsConfig;
    this.eraHistory = eraHistory;
    this.objectMapper = objectMapper;

    var byronGenesis = parseByronGenesisFile(conversionsConfig.genesisPaths().byronLink());
    var shelleyGenesis = parseShelleyGenesisFile(conversionsConfig.genesisPaths().shelleyLink());

    validate(byronGenesis, shelleyGenesis);
  }

  private void validate(ByronGenesis byronGenesis, ShelleyGenesis shelleyGenesis) {
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

    // both genesis files declare the chain start; they must agree
    var shelleySystemStart = Instant.parse(shelleyGenesis.getSystemStart());
    var byronStart = Instant.ofEpochSecond(byronGenesis.getStartTime());
    if (!shelleySystemStart.equals(byronStart)) {
      throw new ConversionRuntimeException(
          "Genesis files disagree on the network start time, byron: %s, shelley systemStart: %s"
              .formatted(byronStart, shelleySystemStart));
    }

    // the two-phase (Byron then Shelley) conversion model relies on Byron epochs having the same
    // wall-clock duration as Shelley epochs; this holds on all Cardano networks
    var byronEpochDuration = byronSlotLength.multipliedBy(byronEpochLength);
    var shelleyEpochDuration = shelleySlotLength.multipliedBy(shelleyEpochLength);
    if (!byronEpochDuration.equals(shelleyEpochDuration)) {
      throw new ConversionRuntimeException(
          "Byron epoch duration: %s does not match Shelley epoch duration: %s"
              .formatted(byronEpochDuration, shelleyEpochDuration));
    }

    var networkTypeEpochLength = conversionsConfig.networkType().getEpochLengthInSlots();
    if (networkTypeEpochLength != shelleyEpochLength) {
      log.warn(
          "NetworkType epoch length: {} does not match shelley genesis epochLength: {},"
              + " genesis file wins",
          networkTypeEpochLength,
          shelleyEpochLength);
    }
  }

  public LocalDateTime getStartTime() {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(cardanoNetworkStartTime), UTC);
  }

  public LocalDateTime getShelleyStartTime() {
    return getStartTime().plusSeconds(firstShelleySlot() * slotDuration(Byron).getSeconds());
  }

  public LocalDateTime blockTime(EraType era, long slot) {
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
   * @param eraType - cardano eraType e.g. Byron, Shelley
   * @return duration of a slot in a given eraType
   */
  public Duration slotDuration(EraType eraType) {
    if (eraType == Byron) {
      return byronSlotLength;
    }

    return shelleySlotLength;
  }

  /**
   * Number of slots per epoch in a given era, taken from the genesis files: 10 * k for Byron,
   * epochLength for Shelley and later eras.
   */
  public long slotsPerEpoch(EraType era) {
    if (era == Byron) {
      return byronEpochLength;
    }

    return shelleyEpochLength;
  }

  public int lastByronEpochNo() {
    return firstShelleyEpochNo() - 1;
  }

  /**
   * First epoch of the post-Byron phase. Networks that never ran Byron (e.g. preview, which
   * launched directly in Alonzo) start in a later era, so this is the start epoch of the first
   * non-Byron era history item.
   */
  public int firstShelleyEpochNo() {
    return eraHistory.all().stream()
        .filter(item -> item.era().eraType() != Byron)
        .findFirst()
        .map(EraHistoryItem::startEpochNo)
        .orElseThrow(() -> new ConversionRuntimeException("No post-Byron era found!"));
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
    byronEpochLength = 10 * byronGenesis.getSecurityParam();
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
