package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Era.Byron;
import static org.cardanofoundation.conversions.domain.Era.Shelley;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.conversions.converters.EpochConversions;
import org.cardanofoundation.conversions.converters.SlotConversions;
import org.cardanofoundation.conversions.converters.TimeConversions;
import org.cardanofoundation.conversions.domain.Era;
import org.cardanofoundation.conversions.domain.NetworkType;

public final class ClasspathConversionsFactory {

  private ClasspathConversionsFactory() {}

  public static ConversionsConfig create(NetworkType networkType) {
    return switch (networkType) {
      case MAINNET, PREPROD -> {
        var loader = ClasspathConversionsFactory.class.getClassLoader();

        var byronLink = loader.getResource(getGenesisEraClasspathLink(Byron, networkType));
        var shelleyLink = loader.getResource(getGenesisEraClasspathLink(Shelley, networkType));

        yield new ConversionsConfig(networkType, byronLink, shelleyLink);
      }

      default -> throw new IllegalStateException("Unsupported network type: " + networkType);
    };
  }

  public static CardanoConverters createConverters(NetworkType networkType) {
    return createConverters(networkType, new ObjectMapper());
  }

  public static CardanoConverters createConverters(
      NetworkType networkType, ObjectMapper objectMapper) {
    var conversionsConfig = ClasspathConversionsFactory.create(networkType);
    var genesisConfig = new GenesisConfig(conversionsConfig, objectMapper);

    var slotConversions = new SlotConversions(genesisConfig);
    var epochConversions = new EpochConversions(genesisConfig, slotConversions);
    var timeConversions = new TimeConversions(genesisConfig, slotConversions);

    return new CardanoConverters(genesisConfig, epochConversions, slotConversions, timeConversions);
  }

  private static String getGenesisEraClasspathLink(Era era, NetworkType networkType) {
    return String.format(
        "genesis-files/%s/%s-genesis.json",
        networkType.toString().toLowerCase(), era.name().toLowerCase());
  }
}