package org.cardanofoundation.conversions.domain;

import java.util.Arrays;
import java.util.Optional;

public enum NetworkType {
  MAINNET(764824073),
  LEGACY_TESTNET(1097911063),
  PREPROD(1),
  PREVIEW(2),
  DEV(42);

  long protocolMagic;

  NetworkType(long protocolMagic) {
    this.protocolMagic = protocolMagic;
  }

  public long getProtocolMagic() {
    return protocolMagic;
  }

  public static Optional<NetworkType> fromProtocolMagic(long protocolMagic) {
    return Arrays.stream(NetworkType.values())
        .filter(networkType -> networkType.getProtocolMagic() == protocolMagic)
        .findFirst();
  }
}
