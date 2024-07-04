package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EraType.Babbage;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.LocalDateTime;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EraConversionsMainNetTest {
  private EraConversions eraConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(MAINNET);
    eraConversions = converters.era();
  }

  @Test
  void firstRealSlotShelley() {
    var absoluteSlot = eraConversions.firstRealSlot(Shelley);
    assertThat(absoluteSlot).isEqualTo(4492800L);
  }

  @Test
  void firstRealSlotBabbage() {
    var absoluteSlot = eraConversions.firstRealSlot(Babbage);
    assertThat(absoluteSlot).isEqualTo(72316896L);
  }

  @Test
  void firstTheoreticalSlotShelley() {
    var absoluteSlot = eraConversions.firstTheoreticalSlot(Shelley);
    assertThat(absoluteSlot).isEqualTo(4492800L);
  }

  @Test
  void firstTheoreticalSlotBabbage() {
    var absoluteSlot = eraConversions.firstTheoreticalSlot(Babbage);
    assertThat(absoluteSlot).isEqualTo(72316800L);
  }

  @Test
  void lastRealSlotBabbage() {
    assertThat(eraConversions.lastRealEraTime(Babbage)).isEmpty();
  }

  @Test
  void lastTheoreticalSlotShelley() {
    var absoluteSlot = eraConversions.lastTheoreticalSlot(Shelley).orElseThrow();
    assertThat(absoluteSlot).isEqualTo(16588799L);
  }

  @Test
  void firstRealEraTimeShelley() {
    var time = eraConversions.firstRealEraTime(Shelley);
    assertThat(time).isEqualTo(LocalDateTime.of(2020, 7, 29, 21, 44, 51));
  }

  @Test
  void lastRealEraTimeShelley() {
    var time = eraConversions.lastRealEraTime(Shelley).orElseThrow();
    assertThat(time).isEqualTo(LocalDateTime.of(2020, 12, 16, 21, 43, 48));
  }

  @Test
  void firstRealEraTimeBabbage() {
    var time = eraConversions.firstRealEraTime(Babbage);
    assertThat(time).isEqualTo(LocalDateTime.of(2022, 9, 22, 21, 46, 27));
  }

  @Test
  void lastRealEraTimeBabbage() {
    assertThat(eraConversions.lastRealEraTime(Babbage)).isEmpty();
  }
}
