package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EraType.*;
import static org.cardanofoundation.conversions.domain.NetworkType.SANCHONET;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class GenesisConfigSanchonetTest {

  private static GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() {
    var conversionsConfig = ClasspathConversionsFactory.create(SANCHONET);
    var eraHistory = StaticEraHistoryFactory.create(conversionsConfig.genesisPaths());

    genesisConfig = new GenesisConfig(conversionsConfig, eraHistory, new ObjectMapper());
  }

  @Test
  public void testByronSlotDuration() {
    assertThat(genesisConfig.slotDuration(Byron)).isEqualTo(Duration.ofSeconds(20));
  }

  @Test
  public void testShelleySlotDuration() {
    assertThat(genesisConfig.slotDuration(Shelley)).isEqualTo(Duration.ofSeconds(1));
  }

  @Test
  public void testAlonzoSlotDuration() {
    assertThat(genesisConfig.slotDuration(Alonzo)).isEqualTo(Duration.ofSeconds(1));
  }

  @Test
  public void testByronSlotsPerEpoch() {
    assertThat(genesisConfig.slotsPerEpoch(Byron)).isEqualTo(4320L);
  }

  @Test
  public void testShelleySlotsPerEpoch() {
    assertThat(genesisConfig.slotsPerEpoch(Shelley)).isEqualTo(86400L);
  }

  @Test
  public void testAllegraSlotsPerEpoch() {
    assertThat(genesisConfig.slotsPerEpoch(Allegra)).isEqualTo(86400L);
  }

  @Test
  @Disabled("there is no byron in preview")
  public void testLastByronEpoch() {
    assertThat(genesisConfig.lastByronEpochNo()).isEqualTo(-1);
  }

  @Test
  public void testFirstShelleyEpoch() {
    assertThat(genesisConfig.firstShelleyEpochNo()).isEqualTo(0);
  }

  @Test
  @Disabled("there is no byron in preview")
  public void testLastByronSlot() {
    assertThat(genesisConfig.lastByronSlot()).isEqualTo(4492799);
  }

  @Test
  public void testPrimitiveNetworkStartTime() {
    assertThat(genesisConfig.getCardanoNetworkStartTime()).isEqualTo(1686789000L);
  }

  @Test
  public void testNetworkStartTime() {
    assertThat(genesisConfig.getStartTime()).isEqualTo(LocalDateTime.of(2023, 6, 15, 0, 30, 0));
  }

  @Test
  public void testProtocolMagic() {
    assertThat(genesisConfig.getProtocolNetworkMagic()).isEqualTo(4L);
  }

  @Test
  public void testBlockTimeNetworkBeginning() {
    assertThat(genesisConfig.blockTime(Byron, 0)).isEqualTo(genesisConfig.getStartTime());
  }

  @Test
  public void testShelleyStartTime() {
    assertThat(genesisConfig.getShelleyStartTime())
        .isEqualTo(LocalDateTime.of(2023, 6, 15, 0, 30, 0));
  }

  @Test
  // proposal reveal time for Cardano Summit Awards 2023
  public void testBlockTimeNetwork() {
    assertThat(genesisConfig.blockTime(Shelley, 26735107L))
        .isEqualTo(LocalDateTime.of(2024, 4, 19, 10, 55, 7));
  }
}
