package org.cardanofoundation.conversions.converters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ConversionRuntimeException;
import org.cardanofoundation.conversions.GenesisConfig;
import org.cardanofoundation.conversions.domain.EraHistoryItem;
import org.cardanofoundation.conversions.domain.EraType;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class EraConversions {

    private final GenesisConfig genesisConfig;
    private final SlotConversions slotConversions;

    public long firstRealSlot(EraType eraType) {
        return getEraHistoryItem(eraType).firstRealSlotNo();
    }

    public long firstTheoreticalSlot(EraType eraType) {
        return getEraHistoryItem(eraType).firstTheoreticalSlotNo();
    }

    public Optional<Long> lastTheoreticalSlot(EraType eraType) {
        return getEraHistoryItem(eraType).lastTheoreticalSlotNo();
    }

    public Optional<Long> lastRealSlot(EraType eraType) {
        return getEraHistoryItem(eraType).lastRealSlotNo();
    }

    public LocalDateTime firstRealEraTime(EraType eraType) {
        var absoluteSlot = firstRealSlot(eraType);

        return slotConversions.slotToTime(absoluteSlot);
    }

    public LocalDateTime firstTheoreticalEraTime(EraType eraType) {
        var absoluteSlot = firstTheoreticalSlot(eraType);

        return slotConversions.slotToTime(absoluteSlot);
    }

    public Optional<LocalDateTime> lastRealEraTime(EraType eraType) {
        return lastRealSlot(eraType)
                .map(slotConversions::slotToTime);
    }

    public Optional<LocalDateTime> lastTheoreticalEraTime(EraType eraType) {
        return lastTheoreticalSlot(eraType)
                .map(slotConversions::slotToTime);
    }

    private EraHistoryItem getEraHistoryItem(EraType eraType) {
        return genesisConfig.getEraHistory()
                .findFirstByEra(eraType)
                .orElseThrow(() -> new ConversionRuntimeException("Era details not found!, era: " + eraType));
    }

}
