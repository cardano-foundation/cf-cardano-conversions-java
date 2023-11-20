package org.cardanofoundation.conversions;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SlotConversions {

  private final GenesisConfig genesisConfig;

  public LocalDateTime slotToTime(long absoluteSlot) {
    // return genesisConfig.getGenesisDate().plusSeconds(slot * genesisConfig.getSlotLength());
    return null;
  }
}
