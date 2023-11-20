package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Consensus.*;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.Praos;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_0_0;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.cardanofoundation.conversions.domain.*;

@AllArgsConstructor
public class EraService {

  private final GenesisConfig genesisConfig;

  public List<EraLine> all(NetworkType networkType) {
    return switch (networkType) {
      case MAINNET -> mainnet();
      case PREPROD -> preprod();
      default -> List.of();
    };
  }

  private List<EraLine> mainnet() {
    return List.of(
        new EraLine(Phase.Byron, Era.Byron, 0, VER_0_0, Praos, Ouroboros_Classic),
        new EraLine(Phase.Byron, Era.Byron, 3801600, ProtocolVersion.VER_1_0, Praos, Ouroboros_BFT),
        new EraLine(
            Phase.Shelley, Era.Shelley, 4492800, ProtocolVersion.VER_2_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Allegra, 16588800, ProtocolVersion.VER_3_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Mary, 23068800, ProtocolVersion.VER_4_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Alonzo, 39916975, ProtocolVersion.VER_5_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Alonzo, 43372972, ProtocolVersion.VER_6_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Babbage, 72316896, ProtocolVersion.VER_7_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Babbage, 84844885, ProtocolVersion.VER_8_0, Praos, Ouroboros_Praos));
  }

  private List<EraLine> preprod() {
    return List.of(
        new EraLine(Phase.Byron, Era.Byron, 0, ProtocolVersion.VER_1_0, Praos, Ouroboros_BFT),
        new EraLine(
            Phase.Shelley, Era.Shelley, 84242 + 1, ProtocolVersion.VER_2_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Allegra, 518360 + 1, ProtocolVersion.VER_3_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Mary, 950340 + 1, ProtocolVersion.VER_4_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen, Era.Alonzo, 1382348 + 1, ProtocolVersion.VER_6_0, Praos, Ouroboros_Praos),
        new EraLine(
            Phase.Gougen,
            Era.Babbage,
            3542390 + 1,
            ProtocolVersion.VER_8_0,
            Praos,
            Ouroboros_Praos));
  }

  public Optional<EraLine> findShelleyEraLine(NetworkType networkType) {
    return all(networkType).stream().filter(eraLine -> eraLine.era() == Era.Shelley).findFirst();
  }
}

// last byron block on pre-prod: 84242
