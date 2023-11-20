package org.cardanofoundation.conversions;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.MalformedURLException;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.ConversionsConfig;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
class SlotConversionsMainNetTest {

  private static SlotConversions slotConversions;

  @BeforeEach
  public void setup() throws MalformedURLException {
    var byronLink = new File("src/main/resources/mainnet/byron-genesis.json").toURL();
    var shelleyLink = new File("src/main/resources/mainnet/shelley-genesis.json").toURL();

    var conversionsConfig = new ConversionsConfig(byronLink, shelleyLink);
    var genesisConfig = new GenesisConfig(conversionsConfig, new ObjectMapper());

    slotConversions = new SlotConversions(genesisConfig);
  }
}
