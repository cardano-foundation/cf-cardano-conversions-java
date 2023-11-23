package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.LocalDateTime;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeConversionsMainNetTest {

  private static TimeConversions timeConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(MAINNET);
    timeConversions = converters.timeConversions();
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
}
