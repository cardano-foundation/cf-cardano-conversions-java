package org.cardanofoundation.conversions;

import org.cardanofoundation.conversions.converters.EpochConversions;
import org.cardanofoundation.conversions.converters.EraConversions;
import org.cardanofoundation.conversions.converters.SlotConversions;
import org.cardanofoundation.conversions.converters.TimeConversions;

public record CardanoConverters(
    ConversionsConfig conversionsConfig,
    GenesisConfig genesisConfig,
    EpochConversions epoch,
    SlotConversions slot,
    TimeConversions time,
    EraConversions era) {}
