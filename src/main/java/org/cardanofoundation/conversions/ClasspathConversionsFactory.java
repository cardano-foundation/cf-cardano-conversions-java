package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.conversions.converters.EpochConversions;
import org.cardanofoundation.conversions.converters.EraConversions;
import org.cardanofoundation.conversions.converters.SlotConversions;
import org.cardanofoundation.conversions.converters.TimeConversions;
import org.cardanofoundation.conversions.domain.EraType;
import org.cardanofoundation.conversions.domain.GenesisPaths;
import org.cardanofoundation.conversions.domain.NetworkType;

public final class ClasspathConversionsFactory {

  private ClasspathConversionsFactory() {}

  public static ConversionsConfig create(NetworkType networkType) {
    return switch (networkType) {
      case MAINNET, PREPROD -> {
        var loader = ClasspathConversionsFactory.class.getClassLoader();

        var byronLink = loader.getResource(getGenesisEraClasspathLink(Byron, networkType));
        var shelleyLink = loader.getResource(getGenesisEraClasspathLink(Shelley, networkType));
        var genesisPaths = new GenesisPaths(networkType, byronLink, shelleyLink);

        yield new ConversionsConfig(networkType, genesisPaths);
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
    var eraHistory = StaticEraHistoryFactory.create(conversionsConfig.genesisPaths());

    var genesisConfig = new GenesisConfig(conversionsConfig, eraHistory, objectMapper);

    var slotConversions = new SlotConversions(genesisConfig);
    var epochConversions = new EpochConversions(genesisConfig, slotConversions);
    var timeConversions = new TimeConversions(genesisConfig, slotConversions);
    var eraConversions = new EraConversions(genesisConfig, slotConversions);

    return new CardanoConverters(
        conversionsConfig,
            genesisConfig,
            epochConversions,
            slotConversions,
            timeConversions,
            eraConversions
    );
  }

  private static String getGenesisEraClasspathLink(EraType era, NetworkType networkType) {
    return String.format(
        "genesis-files/%s/%s-genesis.json",
        networkType.toString().toLowerCase(), era.name().toLowerCase());
  }
}
