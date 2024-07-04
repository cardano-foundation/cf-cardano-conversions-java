package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.LocalDateTime;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TimeConversionsMainNetTest {

  private static TimeConversions timeConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(MAINNET);
    timeConversions = converters.time();
  }

  @Test
  public void testByronEraTime1() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2019, 2, 5, 0, 0, 0)))
        .isEqualTo(99);
  }

  @Test
  public void testByronEraBeforeShelley1() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2020, 7, 29, 21, 44, 10)))
        .isEqualTo(207);
  }

  @Test
  public void testByronEraBeforeShelley2() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2020, 7, 29, 21, 44, 31)))
        .isEqualTo(207);
  }

  @Test
  public void testByronEraAfterByron() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2020, 7, 29, 21, 44, 32)))
        .isEqualTo(208);
  }

  @Test
  public void testByronEraAfterByron1() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2020, 7, 29, 21, 44, 53)))
        .isEqualTo(208);
  }

  @Test
  public void testBabbageEra1() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2023, 11, 22, 9, 48, 58)))
        .isEqualTo(450);
  }

  @Test
  public void dateTimeToSlotBeforeBlockchainStartThrowsError() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> timeConversions.toSlot(LocalDateTime.of(2015, 10, 3, 21, 44, 11)));
  }

  @ParameterizedTest
  @CsvSource({
    "2017-10-03T21:44:11,43198", // Byron
    "2023-11-22T12:47:09,109090938", // Shelley
    "2020-07-29T21:44:31,4492799", // Last Byron
    "2020-07-29T21:44:51,4492800", // First Shelley
  })
  public void slotToEpoch(String dateTime, long epoch) {
    assertThat(timeConversions.toSlot(LocalDateTime.parse(dateTime))).isEqualTo(epoch);
  }
}
