package org.cardanofoundation.conversions.converters;

import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.cardanofoundation.conversions.GenesisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

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
