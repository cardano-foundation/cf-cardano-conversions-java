package org.cardanofoundation.conversions;

import java.util.List;
import org.cardanofoundation.conversions.domain.GenesisPaths;

public class StaticEraHistoryFactory {

  public static EraHistory create(GenesisPaths genesisPaths) {
    return switch (genesisPaths.networkType()) {
      case MAINNET -> new EraHistory(MainNetEraHistoryFactory.eraHistoryItems(genesisPaths));
      case PREPROD -> new EraHistory(PreProdEraHistoryFactory.eraHistoryItems(genesisPaths));
      case PREVIEW -> new EraHistory(PreviewEraHistoryFactory.eraHistoryItems(genesisPaths));
      case SANCHONET -> new EraHistory(SanchoNetEraHistoryFactory.eraHistoryItems(genesisPaths));
      default -> new EraHistory(List.of());
    };
  }
}
