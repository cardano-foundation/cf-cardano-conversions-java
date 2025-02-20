package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

import java.time.LocalDateTime;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TimeConversionsPreProdTest {

  private static TimeConversions timeConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(PREPROD);
    timeConversions = converters.time();
  }

  @Test
  public void testBabbageEra1() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2023, 11, 22, 9, 48, 58)))
        .isEqualTo(107);
  }

  @Test
  public void testBabbageEra2() {
    assertThat(timeConversions.utcTimeToEpochNo(LocalDateTime.of(2023, 11, 23, 12, 48, 58)))
        .isEqualTo(108);
  }

  @ParameterizedTest
  @CsvSource({
    "2024-11-29T15:51:43,77208703" // https://preprod.cardanoscan.io/transaction/1188eb4283ae74e4e62d99ec201cb15ee916d4edb2b70fc8de3cb2824ea461b7
  })
  public void dateToSlot(String dateTime, long absoluteSlot) {
    assertThat(timeConversions.toSlot(LocalDateTime.parse(dateTime))).isEqualTo(absoluteSlot);
  }
}
