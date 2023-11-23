package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.Era.*;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class GenesisConfigMainNetTest {

  private static GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() {
    var conversionsConfig = ClasspathConversionsFactory.create(MAINNET);

    genesisConfig = new GenesisConfig(conversionsConfig, new ObjectMapper());
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
    assertThat(genesisConfig.slotsPerEpoch(Byron)).isEqualTo(21600L);
  }

  @Test
  public void testShelleySlotsPerEpoch() {
    assertThat(genesisConfig.slotsPerEpoch(Shelley)).isEqualTo(432000L);
  }

  @Test
  public void testAllegraSlotsPerEpoch() {
    assertThat(genesisConfig.slotsPerEpoch(Allegra)).isEqualTo(432000L);
  }

  @Test
  public void testLastByronEpoch() {
    assertThat(genesisConfig.lastByronEpochNo()).isEqualTo(207);
  }

  @Test
  public void testFirstShelleyEpoch() {
    assertThat(genesisConfig.firstShelleyEpochNo()).isEqualTo(208);
  }

  @Test
  public void testLastByronSlot() {
    assertThat(genesisConfig.lastByronSlot()).isEqualTo(4492799);
  }

  @Test
  public void testPrimitiveNetworkStartTime() {
    assertThat(genesisConfig.getCardanoNetworkStartTime()).isEqualTo(1506203091L);
  }

  @Test
  public void testNetworkStartTime() {
    assertThat(genesisConfig.getStartTime()).isEqualTo(LocalDateTime.of(2017, 9, 23, 21, 44, 51));
  }

  @Test
  public void testProtocolMagic() {
    assertThat(genesisConfig.getProtocolNetworkMagic()).isEqualTo(764824073L);
  }

  @Test
  public void testBlockTimeNetworkBeginning() {
    assertThat(genesisConfig.blockTime(Byron, 0)).isEqualTo(genesisConfig.getStartTime());
  }

  @Test
  public void testShelleyStartTime() {
    assertThat(genesisConfig.getShelleyStartTime())
        .isEqualTo(LocalDateTime.of(2020, 7, 29, 21, 44, 51));
  }

  @Test
  public void testBlockTimeNetwork1() {
    // https://cardanoscan.io/transaction/5fbf0a6a0dbe1917a0c2bc34faca7d7d2a6f3956856c6c963fda952f3f0da5cf
    assertThat(genesisConfig.blockTime(Babbage, 109076647))
        .isEqualTo(LocalDateTime.of(2023, 11, 22, 8, 48, 58));
  }

  @Test
  // proposal reveal time for Cardano Summit Awards 2023
  public void testBlockTimeNetwork2() {
    assertThat(genesisConfig.blockTime(Shelley, 107576110))
        .isEqualTo(LocalDateTime.of(2023, 11, 5, 0, 0, 01));
  }
}
