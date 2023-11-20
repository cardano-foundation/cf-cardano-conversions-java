package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.Era.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ConversionsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class GenesisConfigMainNetTest {

  private static GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() throws MalformedURLException {
    var byronLink = new File("src/main/resources/mainnet/byron-genesis.json").toURL();
    var shelleyLink = new File("src/main/resources/mainnet/shelley-genesis.json").toURL();

    var conversionsConfig = new ConversionsConfig(byronLink, shelleyLink);

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
}
