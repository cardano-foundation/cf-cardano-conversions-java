package org.cardanofoundation.conversions.domain;

import java.net.URL;
import java.util.Optional;

public record Era(EraType eraType, Optional<URL> genesisLink) {

  public static Era noGenesis(EraType eraType) {
    return new Era(eraType, Optional.empty());
  }
}

// byron era length in slots = 21600
// shelley era length in slots = 432000
