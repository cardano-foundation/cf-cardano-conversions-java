package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Consensus.Ouroboros_Praos;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.Praos;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_6_0;

import java.util.List;
import java.util.Optional;
import org.cardanofoundation.conversions.domain.Era;
import org.cardanofoundation.conversions.domain.EraHistoryItem;
import org.cardanofoundation.conversions.domain.GenesisPaths;
import org.cardanofoundation.conversions.domain.Phase;

public class SanchoNetEraHistoryFactory {

  public static List<EraHistoryItem> eraHistoryItems(GenesisPaths genesisPaths) {
    return List.of(
        new EraHistoryItem(
            Phase.Shelley,
            new Era(Shelley, Optional.of(genesisPaths.shelleyLink())),
            0L, // TODO find out this value based on some explorer data
            Optional.empty(),
            0L,
            Optional.empty(),
            0,
            Optional.empty(),
            VER_6_0,
            Optional.of(Praos),
            Ouroboros_Praos,
            false));
  }
}
