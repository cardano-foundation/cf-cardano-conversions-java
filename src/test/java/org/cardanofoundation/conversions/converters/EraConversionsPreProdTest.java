package org.cardanofoundation.conversions.converters;

import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

class EraConversionsPreProdTest {
    private EraConversions eraConversions;

    @BeforeEach
    public void setup() {
        var converters = ClasspathConversionsFactory.createConverters(PREPROD);
        eraConversions = converters.era();
    }

    @Test
    void firstTheoreticalSlot() {
        var shelleySlot = eraConversions.firstTheoreticalSlot(Shelley);
        assertThat(shelleySlot).isEqualTo(86400L);
    }

    @Test
    void lastTheoreticalSlot() {
        var shelleySlot = eraConversions.lastTheoreticalSlot(Shelley).orElseThrow();
        assertThat(shelleySlot).isEqualTo(518399L);
    }

}