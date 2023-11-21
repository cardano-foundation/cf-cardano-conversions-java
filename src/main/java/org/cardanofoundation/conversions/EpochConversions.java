package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.EpochOffset.END;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.Era.Byron;
import static org.cardanofoundation.conversions.domain.Era.Shelley;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.domain.EpochOffset;
import org.cardanofoundation.conversions.domain.Era;

@AllArgsConstructor
@Slf4j
public final class EpochConversions {

  private final GenesisConfig genesisConfig;

  private final SlotConversions slotConversions;

  /**
   * Returns an absolute slot being pointing to the beginning of an epoch based on a given epochNo.
   *
   * @param epochNo - epoch number, e.g. 208
   * @return absolute slot of the beginning of an epoch
   */
  public long beginningOfEpochToAbsoluteSlot(int epochNo) {
    return epochToAbsoluteSlot(epochNo, START);
  }

  /**
   * Returns an absolute slot pointing to the end of an epoch based on a given epoch number.
   *
   * @param epochNo - epoch number, e.g. 208
   * @return absolute slot of the end of an epoch
   */
  public long endingOfEpochToAbsoluteSlot(int epochNo) {
    return epochToAbsoluteSlot(epochNo, END);
  }

  /**
   * Converts epoch number to absolute slot.
   *
   * @param epochNo - epoch number, e.g. 208
   * @param epochOffset - either epoch's start or epoch's end
   * @return absolute slot of a given epoch
   */
  public long epochToAbsoluteSlot(int epochNo, EpochOffset epochOffset) {
    return switch (epochOffset) {
      case START -> {
        if (epochNo <= genesisConfig.lastByronEpochNo()) {
          yield absoluteSlotAssumingEra(Byron, epochNo, END)
              - genesisConfig.slotsPerEpoch(Byron)
              + 1;
        }

        yield epochToAbsoluteSlot(epochNo, END) - genesisConfig.slotsPerEpoch(Shelley);
      }
      case END -> {
        if (epochNo <= genesisConfig.lastByronEpochNo()) {
          yield absoluteSlotAssumingEra(Byron, epochNo, END);
        }

        var lastByronSlot = genesisConfig.lastByronSlot();
        var countLastByronSlot = lastByronSlot + 1;

        var postByronEpochs = (epochNo - genesisConfig.lastByronEpochNo() - 1);

        yield 1 + countLastByronSlot + absoluteSlotAssumingEra(Shelley, postByronEpochs, END);
      }
    };
  }

  /**
   * Converts epoch number to UTC time.
   *
   * @param epochNo - epoch number, e.g. 208
   * @param epochOffset - either epoch's start or epoch's end
   * @return UTC time of a given epoch
   */
  public LocalDateTime epochToUTCTime(int epochNo, EpochOffset epochOffset) {
    var absoluteSlot = epochToAbsoluteSlot(epochNo, epochOffset);

    return slotConversions.slotToTime(absoluteSlot);
  }

  /**
   * Returns absolute slot but assuming one is in particular era.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @param epochOffset - start or end of a given epoch
   * @return absolute slot of a given epoch
   */
  long absoluteSlotAssumingEra(Era era, int epochNo, EpochOffset epochOffset) {
    long allSlotsPerEra = genesisConfig.slotsPerEpoch(era) * epochNo;

    return switch (epochOffset) {
      case START -> allSlotsPerEra;
      case END -> allSlotsPerEra + ((genesisConfig.slotsPerEpoch(era)) - 1);
    };
  }

  /**
   * Returns first slot of a given epoch.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @return first slot of a given epoch
   */
  long firstEpochSlot(Era era, int epochNo) {
    return absoluteSlotAssumingEra(era, epochNo, START);
  }

  /**
   * Returns last slot of a given epoch.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @return last slot of a given epoch
   */
  long lastEpochSlot(Era era, int epochNo) {
    return absoluteSlotAssumingEra(era, epochNo, END);
  }
}
