package org.cardanofoundation.conversions.converters;

import static org.cardanofoundation.conversions.domain.EpochOffset.END;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.GenesisConfig;
import org.cardanofoundation.conversions.domain.EpochOffset;
import org.cardanofoundation.conversions.domain.EraType;

@AllArgsConstructor
@Slf4j
public final class EpochConversions {

  private final GenesisConfig genesisConfig;

  private final SlotConversions slotConversions;

  /**
   * Returns an absolute slot being pointing to the beginning of an epoch based on a given
   * startEpochNo.
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
    return epochToAbsoluteSlot(epochNo);
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
          yield (absoluteSlotAssumingEra(EraType.Byron, epochNo)
                  - genesisConfig.slotsPerEpoch(Byron))
              + 1;
        }

        yield (epochToAbsoluteSlot(epochNo) - genesisConfig.slotsPerEpoch(Shelley)) + 1;
      }
      case END -> epochToAbsoluteSlot(epochNo);
    };
  }

  /**
   * Canonical implementation of epochToAbsoluteSlot function.
   *
   * @param epochNo epoch number
   * @return absolute slot
   */
  long epochToAbsoluteSlot(int epochNo) {
    if (epochNo <= genesisConfig.lastByronEpochNo()) {
      return absoluteSlotAssumingEra(Byron, epochNo);
    }

    var firstShelleySlot = genesisConfig.firstShelleySlot();

    var postByronEpochs = (epochNo - genesisConfig.lastByronEpochNo() - 1);

    return firstShelleySlot + absoluteSlotAssumingEra(Shelley, postByronEpochs);
  }

  /**
   * Converts epoch number to the UTC Time at the beginning of given epoch
   *
   * @param epochNo epoch number
   * @return UTC time
   */
  public LocalDateTime beginningOfEpochToUTCTime(int epochNo) {
    return epochToUTCTime(epochNo, START);
  }

  /**
   * Converts epoch number to the UTC Time at the end of a given epoch
   *
   * @param epochNo epoch number
   * @return UTC time
   */
  public LocalDateTime endingOfEpochToUTCTime(int epochNo) {
    return epochToUTCTime(epochNo, END);
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
  long absoluteSlotAssumingEra(EraType era, int epochNo, EpochOffset epochOffset) {
    long allSlotsPerEra = genesisConfig.slotsPerEpoch(era) * epochNo;

    return switch (epochOffset) {
      case START -> allSlotsPerEra;
      case END -> absoluteSlotAssumingEra(era, epochNo);
    };
  }

  long absoluteSlotAssumingEra(EraType era, int epochNo) {
    long allSlotsPerEra = genesisConfig.slotsPerEpoch(era) * epochNo;

    return allSlotsPerEra + ((genesisConfig.slotsPerEpoch(era)) - 1);
  }

  /**
   * Returns first slot of a given epoch based on era.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @return first slot of a given epoch
   */
  long firstEpochSlot(EraType era, int epochNo) {
    return absoluteSlotAssumingEra(era, epochNo, START);
  }

  /**
   * Returns last slot of a given epoch based on era.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @return last slot of a given epoch
   */
  long lastEpochSlot(EraType era, int epochNo) {
    return absoluteSlotAssumingEra(era, epochNo, END);
  }
}
