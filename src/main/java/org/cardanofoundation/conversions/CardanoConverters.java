package org.cardanofoundation.conversions;

import org.cardanofoundation.conversions.converters.EpochConversions;
import org.cardanofoundation.conversions.converters.SlotConversions;
import org.cardanofoundation.conversions.converters.TimeConversions;

public record CardanoConverters(
    GenesisConfig genesisConfig,
    EpochConversions epochConversions,
    SlotConversions slotConversions,
    TimeConversions timeConversions) {}
