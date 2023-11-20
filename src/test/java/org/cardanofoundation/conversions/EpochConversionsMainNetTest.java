package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.EpochOffset.END;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.Era.Byron;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ConversionsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
/**
 * Sources: - https://cips.cardano.org/cips/cip59/feature-table.md.html -
 * https://cardanosolutions.github.io/kupo/#section/Rollbacks-and-chain-forks/How-Kupo-deals-with-rollbacks
 */
class EpochConversionsMainNetTest {

  private EpochConversions epochConversions;

  private GenesisConfig genesisConfig;

  @BeforeEach
  public void setup() throws MalformedURLException {
    var byronLink = new File("src/main/resources/mainnet/byron-genesis.json").toURL();
    var shelleyLink = new File("src/main/resources/mainnet/shelley-genesis.json").toURL();

    var conversionsConfig = new ConversionsConfig(byronLink, shelleyLink);

    genesisConfig = new GenesisConfig(conversionsConfig, new ObjectMapper());
    epochConversions = new EpochConversions(genesisConfig);
  }

  @Test
  public void testIntraByronFork() {
    var slot = 3801600L;

    assertThat(epochConversions.epochToAbsoluteSlot(176, START)).isEqualTo(slot);
  }

  @Test
  public void testConvertByronEpochToSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(207, START)).isEqualTo(4471200);
    assertThat(epochConversions.epochToAbsoluteSlot(207, END)).isEqualTo(4492799L);
  }

  @Test
  public void testFirstByronAbsoluteSlot() {
    var beginningOfLastByronSlot = 4471200L;

    assertThat(epochConversions.firstEpochSlot(Byron, 207)).isEqualTo(beginningOfLastByronSlot);
    assertThat(epochConversions.epochToAbsoluteSlot(207, START))
        .isEqualTo(beginningOfLastByronSlot);
    assertThat(epochConversions.beginningOfEpochToAbsoluteSlot(207))
        .isEqualTo(beginningOfLastByronSlot);
  }

  @Test
  public void testByronAbsoluteSlots() {
    assertThat(epochConversions.epochToAbsoluteSlot(108, START)).isEqualTo(2332800L);
    assertThat(epochConversions.epochToAbsoluteSlot(108, END)).isEqualTo(2354399L);
  }

  @Test
  public void testLastByronAbsoluteSlot() {
    long lastByronSlot = 4492799L;

    assertThat(epochConversions.lastEpochSlot(Byron, 207)).isEqualTo(lastByronSlot);
    assertThat(epochConversions.epochToAbsoluteSlot(207, END)).isEqualTo(lastByronSlot);
    assertThat(epochConversions.endingOfEpochToAbsoluteSlot(207)).isEqualTo(lastByronSlot);
  }

  @Test
  public void testFirstShelleyAbsoluteSlots() {
    var firstShelleySlot = genesisConfig.firstShelleySlot();

    assertThat(epochConversions.epochToAbsoluteSlot(208, START)).isEqualTo(firstShelleySlot);
  }

  @Test
  public void testLastShelleyAbsoluteSlots() {
    assertThat(epochConversions.epochToAbsoluteSlot(235, END)).isEqualTo((16588800L));
  }

  @Test
  public void testFirstAllegraSlot() {
    var firstAllegraSlot = (epochConversions.epochToAbsoluteSlot(235, END));

    assertThat(epochConversions.epochToAbsoluteSlot(236, START)).isEqualTo(firstAllegraSlot);
  }

  @Test
  public void testFirstMarySlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(251, START)).isEqualTo(23068800L);
  }

  @Test
  public void testLastMarySlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(289, END)).isEqualTo(39916800L);
  }

  @Test
  public void testFirstAlonzoSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(290, START)).isEqualTo(39916800L);
  }

  @Test
  public void testSecondAlonzoSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(298, START)).isEqualTo(43372800L);
  }

  @Test
  public void testFirstBabbageSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(365, START)).isEqualTo(72316800L);
  }

  @Test
  public void testSecondBabbageSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(394, START)).isEqualTo(84844800L);
  }

  @Test
  public void testLastAlonzoSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(364, END)).isEqualTo(72316800L);
  }

  @Test
  public void testEpoch448LastSlot() {
    assertThat(epochConversions.epochToAbsoluteSlot(448, END)).isEqualTo(108604800L);
  }

  @Test
  public void testPostShelleyAbsoluteSlotsStart() {
    assertThat(epochConversions.epochToAbsoluteSlot(448, START)).isEqualTo(108172800L);
  }
}
