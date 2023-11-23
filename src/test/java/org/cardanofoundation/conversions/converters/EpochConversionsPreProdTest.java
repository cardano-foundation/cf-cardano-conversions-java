package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EpochOffset.END;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class EpochConversionsPreProdTest {

  private EpochConversions epochConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(PREPROD);
    epochConversions = converters.epochConversions();
  }

  @Test
  public void testEpochBabbage107EndTime() {
    assertThat(epochConversions.epochToUTCTime(107, END))
        .isEqualTo(LocalDateTime.of(2023, 11, 22, 23, 59, 59));
  }

  @Test
  public void testEpochBabbage108StartTime() {
    assertThat(epochConversions.epochToUTCTime(108, START))
        .isEqualTo(LocalDateTime.of(2023, 11, 23, 0, 0, 0));
  }

  @Test
  public void testEpochBabbage103EndTime() {
    assertThat(epochConversions.epochToUTCTime(103, END))
        .isEqualTo(LocalDateTime.of(2023, 11, 2, 23, 59, 59));
  }
}
