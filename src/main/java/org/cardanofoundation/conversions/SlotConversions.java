package org.cardanofoundation.conversions;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.domain.Era;

@RequiredArgsConstructor
public class SlotConversions {

  private final GenesisConfig genesisConfig;

  public LocalDateTime slotToTime(long absoluteSlot) {
    var lastByronSlot = genesisConfig.lastByronSlot();

    if (absoluteSlot <= lastByronSlot) {
      return genesisConfig.blockTime(Era.Byron, absoluteSlot);
    }

    // for now post byron we have 1 slot = 1 second
    return genesisConfig.blockTime(Era.Shelley, absoluteSlot);
  }
}
