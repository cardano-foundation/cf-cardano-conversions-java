package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.cardanofoundation.conversions.GenesisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

  @Test
  public void slot1IsEpoch0() {
    assertThat(slotConversions.slotToEpoch(1)).isEqualTo(0);
  }

  @Test
  public void slot21599IsEpoch0() {
    assertThat(slotConversions.slotToEpoch(21599)).isEqualTo(0);
  }

  @Test
  public void slot21600IsEpoch1() {
    assertThat(slotConversions.slotToEpoch(21600)).isEqualTo(1);
  }

  @Test
  public void testLastByronSlot() {
    var slot = 4492799L;
    assertThat(slotConversions.slotToEpoch(slot)).isEqualTo(207);
  }

  @Test
  public void testFirstShelleySlot() {
    var slot = 4492800L;
    assertThat(slotConversions.slotToEpoch(slot)).isEqualTo(208);
  }

  @Test
  public void testEpoch208() {
    var slot = 4492801L;
    assertThat(slotConversions.slotToEpoch(slot)).isEqualTo(208);
  }

  @Test
  public void testEpoch209() {
    var slot = 4492800L + genesisConfig.getShelleyEpochLength();
    assertThat(slotConversions.slotToEpoch(slot)).isEqualTo(209);
  }

  @Test
  public void testEpoch300() {
    var slot = 44237054L;
    assertThat(slotConversions.slotToEpoch(slot)).isEqualTo(300);
  }
}
