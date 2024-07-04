package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.cardanofoundation.conversions.GenesisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
class SlotConversionsMainNetTest {

  private static SlotConversions slotConversions;

  private static GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(MAINNET);
    genesisConfig = converters.genesisConfig();
    slotConversions = converters.slot();
  }

  @Test
  public void testLastByronBlock() {
    var slot = genesisConfig.lastByronSlot();

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2020, 7, 29, 21, 44, 31));
  }

  @Test
  public void testOneOfFirstByronTransactions() {
    var slot = 43198L;

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2017, 10, 3, 21, 44, 11));
  }

  @Test
  public void testSlot109090938() {
    var slot = 109090938L;

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2023, 11, 22, 12, 47, 9));
  }

  @ParameterizedTest
  @CsvSource({
    "1,0",
    "21599,0", // last slot epoch 0
    "21600,1", // first slot epoch 1
    "4492799,207", // last byron slot
    "4492800,208", // first shelley slot
    "4492801,208",
    "4924800,209",
    "44237054,300",
  })
  public void slotToEpoch(long slot, long epoch) {
    assertThat(slotConversions.slotToEpoch(slot)).isEqualTo(epoch);
  }
}
