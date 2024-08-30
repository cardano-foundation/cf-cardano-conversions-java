package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.NetworkType.PREVIEW;

import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class EpochConversionsPreviewTest {

  private EpochConversions epochConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(PREVIEW);
    epochConversions = converters.epoch();
  }

  @Test
  public void testConwayEraStart() {
    assertThat(epochConversions.epochToAbsoluteSlot(646, START)).isEqualTo(55814400);
  }
}
