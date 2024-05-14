package org.cardanofoundation.conversions.converters;

import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.GenesisConfig;
import org.cardanofoundation.conversions.domain.EraType;

import java.time.LocalDateTime;

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

    public Long slotToEpoch(long absoluteSlot) {

        var shelleyRelativeSlot = absoluteSlot - genesisConfig.firstShelleySlot();
        var numFullEpochs = shelleyRelativeSlot / genesisConfig.getShelleyEpochLength();

        return genesisConfig.firstShelleyEpochNo() + numFullEpochs;

    }

}
