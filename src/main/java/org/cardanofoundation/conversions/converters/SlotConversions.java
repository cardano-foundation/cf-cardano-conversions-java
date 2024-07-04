package org.cardanofoundation.conversions.converters;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.GenesisConfig;
import org.cardanofoundation.conversions.domain.EraType;

@RequiredArgsConstructor
public class SlotConversions {

  private final GenesisConfig genesisConfig;

  /**
   * Convert absolute slot to UTC time.
   *
   * @param absoluteSlot absolute slot
   * @return UTC time
   */
  public LocalDateTime slotToTime(long absoluteSlot) {
    var lastByronSlot = genesisConfig.lastByronSlot();

    if (absoluteSlot <= lastByronSlot) {
      return genesisConfig.blockTime(EraType.Byron, absoluteSlot);
    }

    // for now post byron we have 1 slot = 1 second
    return genesisConfig.blockTime(EraType.Shelley, absoluteSlot);
  }

  /**
   * Computes the epoch a slot falls in.
   *
   * @param absoluteSlot the slot number to convert
   * @return the Era number the slot falls in
   */
  public Long slotToEpoch(long absoluteSlot) {
    if (absoluteSlot < 0L) {
      throw new IllegalArgumentException("absoluteSlot cannot be negative");
    }

    long firstShelleySlot = genesisConfig.firstShelleySlot();

    if (absoluteSlot < firstShelleySlot) {
      return absoluteSlot / genesisConfig.slotsPerEpoch(EraType.Byron);
    } else {
      var shelleyRelativeSlot = absoluteSlot - firstShelleySlot;
      var numFullEpochs = shelleyRelativeSlot / genesisConfig.getShelleyEpochLength();
      return genesisConfig.firstShelleyEpochNo() + numFullEpochs;
    }
  }
}
