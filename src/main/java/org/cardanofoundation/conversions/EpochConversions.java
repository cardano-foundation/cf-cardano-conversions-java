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

    /**
     * @param epochNo
     * @return
     */
    public long beginningOfEpochToAbsoluteSlot(int epochNo) {
        return epochToAbsoluteSlot(epochNo, START);
    }

    /**
     * @param epochNo
     * @return
     */
    public long endingOfEpochToAbsoluteSlot(int epochNo) {
        return epochToAbsoluteSlot(epochNo, END);
    }

    /**
     * Converts epoch to absolute slot.
     *
     * @param epochNo
     * @param epochOffset - either epoch's start or epoch's end
     * @return
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

    public LocalDateTime epochToTime(int epochNo,
                                     EpochOffset epochOffset) {
        //        if (epochNo < 208) {
        //            return genesisConfig.getByronSlotLength() * epochNo;
        //        }
        //

        return null;
    }

    /**
     * Returns absolute slot but assuming one is in particular era.
     *
     * @param era
     * @param epochNo
     * @param epochOffset - start or end of a given epoch
     * @return
     */
    long absoluteSlotAssumingEra(Era era, int epochNo, EpochOffset epochOffset) {
        long allSlotsPerEra = genesisConfig.slotsPerEpoch(era) * epochNo;

        return switch (epochOffset) {
            case START -> allSlotsPerEra;
            case END -> allSlotsPerEra + ((genesisConfig.slotsPerEpoch(era)) - 1);
        };
    }

    long firstEpochSlot(Era era, int epochNo) {
        return absoluteSlotAssumingEra(era, epochNo, START);
    }

    long lastEpochSlot(Era era, int epochNo) {
        return absoluteSlotAssumingEra(era, epochNo, END);
    }
}
