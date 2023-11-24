package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EpochOffset.END;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.cardanofoundation.conversions.GenesisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class EpochConversionsMainNetTest {

  private EpochConversions epochConversions;

  private GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(MAINNET);
    genesisConfig = converters.genesisConfig();
    epochConversions = converters.epoch();
  }

  @Test
  public void testIntraByronFork() {
    var slot = 3801600L;

    assertThat(epochConversions.epochToAbsoluteSlot(176, START)).isEqualTo(slot);
  }

  @Test
  public void testConvertByronEpochToSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(207, START)).isEqualTo(4471200);
    assertThat(epochConversions.epochToAbsoluteSlot(207, END)).isEqualTo(4492799L);
  }

  @Test
  public void testFirstByronAbsoluteSlot() {
    var beginningOfLastByronSlot = 4471200L;

    assertThat(epochConversions.firstEpochSlot(Byron, 207)).isEqualTo(beginningOfLastByronSlot);
    assertThat(epochConversions.epochToAbsoluteSlot(207, START))
        .isEqualTo(beginningOfLastByronSlot);
    assertThat(epochConversions.beginningOfEpochToAbsoluteSlot(207))
        .isEqualTo(beginningOfLastByronSlot);
  }

  @Test
  public void testByronAbsoluteSlots() {
    assertThat(epochConversions.epochToAbsoluteSlot(108, START)).isEqualTo(2332800L);
    assertThat(epochConversions.epochToAbsoluteSlot(108, END)).isEqualTo(2354399L);
  }

  @Test
  public void testLastByronAbsoluteSlot() {
    long lastByronSlot = 4492799L;

    assertThat(epochConversions.lastEpochSlot(Byron, 207)).isEqualTo(lastByronSlot);
    assertThat(epochConversions.epochToAbsoluteSlot(207, END)).isEqualTo(lastByronSlot);
    assertThat(epochConversions.endingOfEpochToAbsoluteSlot(207)).isEqualTo(lastByronSlot);
  }

  @Test
  public void testFirstShelleyAbsoluteSlots() {
    var firstShelleySlot = genesisConfig.firstShelleySlot();

    assertThat(epochConversions.epochToAbsoluteSlot(208, START)).isEqualTo(firstShelleySlot);
  }

  @Test
  public void testLastShelleyAbsoluteSlots() {
    assertThat(epochConversions.epochToAbsoluteSlot(235, END)).isEqualTo((16588799L));
  }

  @Test
  public void testFirstAllegraSlot() {
    var firstAllegraSlot = 16588800;

    assertThat(epochConversions.epochToAbsoluteSlot(236, START)).isEqualTo(firstAllegraSlot);
  }

  @Test
  public void testFirstMarySlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(251, START)).isEqualTo(23068800L);
  }

  @Test
  public void testLastMarySlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(289, END)).isEqualTo(39916799L);
  }

  @Test
  public void testFirstAlonzoSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(290, START)).isEqualTo(39916800L);
  }

  @Test
  public void testSecondAlonzoSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(298, START)).isEqualTo(43372800L);
  }

  @Test
  public void testFirstBabbageSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(365, START)).isEqualTo(72316800L);
  }

  @Test
  public void testSecondBabbageSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(394, START)).isEqualTo(84844800L);
  }

  @Test
  public void testLastAlonzoSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(364, END)).isEqualTo(72316799L);
  }

  @Test
  public void testEpoch448LastSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(448, END)).isEqualTo(108604799L);
  }

  @Test
  public void testPostShelleyAbsoluteSlotsStart() {
    assertThat(epochConversions.epochToAbsoluteSlot(448, START)).isEqualTo(108172800L);
  }

  @Test
  public void testShelleyEraEpochStartTime() {
    assertThat(epochConversions.epochToUTCTime(208, START))
        .isEqualTo(LocalDateTime.of(2020, 7, 29, 21, 44, 51));
  }

  @Test
  public void testShelleyEraEpochEndTime() {
    assertThat(epochConversions.epochToUTCTime(208, END))
        .isEqualTo(LocalDateTime.of(2020, 8, 3, 21, 44, 50));
  }

  @Test
  public void testEpochByronEpoch1StartTime() {
    assertThat(epochConversions.epochToUTCTime(1, START))
        .isEqualTo(LocalDateTime.of(2017, 9, 28, 21, 44, 51));
  }

  @Test
  public void testEpochBabbage445StartTime() {
    assertThat(epochConversions.epochToUTCTime(445, START))
        .isEqualTo(LocalDateTime.of(2023, 10, 27, 21, 44, 51));
  }

  @Test
  public void testEpochBabbage445EndTime() {
    assertThat(epochConversions.epochToUTCTime(445, END))
        .isEqualTo(LocalDateTime.of(2023, 11, 1, 21, 44, 50));
  }

  @Test
  public void testEpochBabbage450StartTime() {
    assertThat(epochConversions.epochToUTCTime(450, START))
        .isEqualTo(LocalDateTime.of(2023, 11, 21, 21, 44, 51));
  }
}
