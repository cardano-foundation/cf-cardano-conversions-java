package org.cardanofoundation.conversions.converters;

import static org.cardanofoundation.conversions.domain.Era.Byron;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.GenesisConfig;
import org.cardanofoundation.conversions.domain.Era;

@RequiredArgsConstructor
public class TimeConversions {

  private final GenesisConfig genesisConfig;

  private final SlotConversions slotsConversions;

  public int utcTimeToEpochNo(LocalDateTime utcTime) {
    var lastGenesisSlot = genesisConfig.lastByronSlot();
    var lastByronSlotTime = slotsConversions.slotToTime(lastGenesisSlot);

    if (utcTime.isBefore(lastByronSlotTime)) {
      return utcTimeToEpochNo(Byron, utcTime);
    }

    var lastByronEpoch = genesisConfig.lastByronEpochNo();
    var lastByronTime = slotsConversions.slotToTime(genesisConfig.lastByronSlot());

    var diffDuration = Duration.between(lastByronTime, utcTime);
    var diffDurationSeconds = diffDuration.getSeconds();
    var slotsPerEpoch = genesisConfig.slotsPerEpoch(Era.Shelley);
    var shelleyEraLength = genesisConfig.getShelleySlotLength();
    var shelleyEraLengthSeconds = shelleyEraLength.getSeconds();

    var shelleyEpochs =
        (int) Math.ceil((double) diffDurationSeconds / slotsPerEpoch / shelleyEraLengthSeconds);

    return lastByronEpoch + shelleyEpochs;
  }

  int utcTimeToEpochNo(Era era, LocalDateTime utcTime) {
    var diffDuration = Duration.between(genesisConfig.getStartTime(), utcTime);
    var diffDurationSeconds = diffDuration.getSeconds();
    var slotsPerEpoch = genesisConfig.slotsPerEpoch(era);
    var byronSlotsLength = genesisConfig.getByronSlotLength();
    var byronSlotsLengthSeconds = byronSlotsLength.getSeconds();

    return (int)
        Math.ceil((double) (diffDurationSeconds / slotsPerEpoch / byronSlotsLengthSeconds));
  }
}
