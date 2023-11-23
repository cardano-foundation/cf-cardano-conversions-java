package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

import java.time.LocalDateTime;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeConversionsPreProdTest {

  private static TimeConversions timeConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(PREPROD);
    timeConversions = converters.timeConversions();
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
}
