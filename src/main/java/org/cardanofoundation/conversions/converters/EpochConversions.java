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
    return epochToAbsoluteSlot(epochNo, END);
  }

  /**
   * Converts epoch number to absolute slot.
   *
   * @param epochNo - epoch number, e.g. 208
   * @param epochOffset - either epoch's start (first slot) or epoch's end (last slot)
   * @return absolute slot of a given epoch
   */
  public long epochToAbsoluteSlot(int epochNo, EpochOffset epochOffset) {
    if (epochNo < 0) {
      throw new IllegalArgumentException("epochNo cannot be negative");
    }

    var era = epochNo <= genesisConfig.lastByronEpochNo() ? Byron : Shelley;
    var firstSlot = firstSlotOfEpoch(epochNo);

    return switch (epochOffset) {
      case START -> firstSlot;
      case END -> firstSlot + genesisConfig.slotsPerEpoch(era) - 1;
    };
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
   * Converts epoch number to the UTC Time at the end of a given epoch, i.e. the time at which the
   * last slot of the epoch starts.
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
   * @return UTC time of a given epoch's first (START) or last (END) slot
   */
  public LocalDateTime epochToUTCTime(int epochNo, EpochOffset epochOffset) {
    var absoluteSlot = epochToAbsoluteSlot(epochNo, epochOffset);

    return slotConversions.slotToTime(absoluteSlot);
  }

  /**
   * Returns the first absolute slot of a given epoch.
   *
   * <p>Byron epochs lie back to back from slot 0; Shelley(+) epochs lie back to back from the first
   * Shelley slot. The Byron-to-Shelley transition happened on an epoch boundary on every network,
   * so the two segments are contiguous.
   */
  private long firstSlotOfEpoch(int epochNo) {
    if (epochNo <= genesisConfig.lastByronEpochNo()) {
      return epochNo * genesisConfig.slotsPerEpoch(Byron);
    }

    var postByronEpochs = epochNo - genesisConfig.firstShelleyEpochNo();

    return genesisConfig.firstShelleySlot()
        + postByronEpochs * genesisConfig.slotsPerEpoch(Shelley);
  }

  /**
   * Returns first slot of a given epoch, assuming the whole chain ran with the slot/epoch
   * parameters of the given era.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @return first slot of a given epoch
   */
  long firstEpochSlot(EraType era, int epochNo) {
    return genesisConfig.slotsPerEpoch(era) * epochNo;
  }

  /**
   * Returns last slot of a given epoch, assuming the whole chain ran with the slot/epoch parameters
   * of the given era.
   *
   * @param era - cardano era e.g. Byron, Shelley
   * @param epochNo - epoch number, e.g. 208
   * @return last slot of a given epoch
   */
  long lastEpochSlot(EraType era, int epochNo) {
    return firstEpochSlot(era, epochNo) + genesisConfig.slotsPerEpoch(era) - 1;
  }
}
