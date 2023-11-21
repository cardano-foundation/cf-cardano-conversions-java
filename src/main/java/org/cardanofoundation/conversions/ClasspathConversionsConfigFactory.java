package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Era.Byron;
import static org.cardanofoundation.conversions.domain.Era.Shelley;

import org.cardanofoundation.conversions.domain.Era;
import org.cardanofoundation.conversions.domain.NetworkType;

public final class ClasspathConversionsConfigFactory {

  private ClasspathConversionsConfigFactory() {}

  public static ConversionsConfig create(NetworkType networkType) {
    return switch (networkType) {
      case MAINNET, PREPROD -> {
        var loader = ClasspathConversionsConfigFactory.class.getClassLoader();

        var byronLink = loader.getResource(getGenesisEraClasspathLink(Byron, networkType));
        var shelleyLink = loader.getResource(getGenesisEraClasspathLink(Shelley, networkType));

        yield new ConversionsConfig(networkType, byronLink, shelleyLink);
      }

      default -> throw new IllegalStateException("Unsupported network type: " + networkType);
    };
  }

  private static String getGenesisEraClasspathLink(Era era, NetworkType networkType) {
    return String.format(
        "genesis-files/%s/%s-genesis.json",
        networkType.toString().toLowerCase(), era.name().toLowerCase());
  }
}
