package org.cardanofoundation.conversions.domain;

public enum NetworkType {
  MAINNET(764_824_073L, 432_000L),
  LEGACY_TESTNET(1_097_911_063L, 432_000L),
  PREPROD(1L, 432_000L),
  PREVIEW(2L, 86_400L),
  DEV(42L, 432_000L);

  private final long protocolMagic;

  private final long epochLengthInSlots;

  NetworkType(long protocolMagic, long epochLengthInSlots) {
    this.protocolMagic = protocolMagic;
    this.epochLengthInSlots = epochLengthInSlots;
  }

  public long getProtocolMagic() {
    return protocolMagic;
  }

  public long getEpochLengthInSlots() {
    return epochLengthInSlots;
  }
}
