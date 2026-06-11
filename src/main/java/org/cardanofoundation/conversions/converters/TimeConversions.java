package org.cardanofoundation.conversions.converters;

import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.GenesisConfig;

@RequiredArgsConstructor
public class TimeConversions {

  private final GenesisConfig genesisConfig;

  private final SlotConversions slotConversions;

  /**
   * Based on UTC time find out what is the epoch number.
   *
   * @param utcTime - UTC time, must not be before the network start time
   * @return epoch number containing this time
   */
  public int utcTimeToEpochNo(LocalDateTime utcTime) {
    return slotConversions.slotToEpoch(toSlot(utcTime)).intValue();
  }

  /**
   * @param utcTime the time to convert into a slot, must not be before the network start time
   * @return the slot containing the time passed as input (this might not coincide with a block)
   */
  public Long toSlot(LocalDateTime utcTime) {
    if (utcTime.isBefore(genesisConfig.getStartTime())) {
      throw new IllegalArgumentException(
          "time: %s falls before the start of the blockchain: %s"
              .formatted(utcTime, genesisConfig.getStartTime()));
    }

    if (utcTime.isBefore(genesisConfig.getShelleyStartTime())) {
      var secondsSinceByronBegin =
          ChronoUnit.SECONDS.between(genesisConfig.getStartTime(), utcTime);

      return secondsSinceByronBegin / genesisConfig.slotDuration(Byron).getSeconds();
    }

    var secondsSinceShelleyBegin =
        ChronoUnit.SECONDS.between(genesisConfig.getShelleyStartTime(), utcTime);

    return genesisConfig.firstShelleySlot()
        + secondsSinceShelleyBegin / genesisConfig.slotDuration(Shelley).getSeconds();
  }
}
