package org.cardanofoundation.conversions.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class SlotConversionsPreProdTest {

  private static SlotConversions slotConversions;

  @BeforeEach
  public void setup() {
    var converters = ClasspathConversionsFactory.createConverters(PREPROD);
    slotConversions = converters.slotConversions();
  }

  @Test
  public void testSlot45056350L() {
    var slot = 45056350L;

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2023, 11, 23, 11, 39, 10));
  }

  @Test
  public void testSlot43760099() {
    var slot = 43760099;

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2023, 11, 8, 11, 34, 59));
  }

  @Test
  public void testSlot43760159() {
    var slot = 43760159;

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2023, 11, 8, 11, 35, 59));
  }
}
