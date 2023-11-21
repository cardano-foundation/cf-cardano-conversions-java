package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class SlotConversionsMainNetTest {

  private static SlotConversions slotConversions;

  private static GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() throws MalformedURLException {
    var conversionsConfig = ClasspathConversionsConfigFactory.create(MAINNET);

    genesisConfig = new GenesisConfig(conversionsConfig, new ObjectMapper());

    slotConversions = new SlotConversions(genesisConfig);
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
    var slot = 109090938;

    assertThat(slotConversions.slotToTime(slot))
        .isEqualTo(LocalDateTime.of(2023, 11, 22, 12, 47, 9));
  }
}
