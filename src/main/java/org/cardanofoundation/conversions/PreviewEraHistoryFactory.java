package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Consensus.Ouroboros_Praos;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.Praos;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_6_0;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_9_1;

import java.util.List;
import java.util.Optional;
import org.cardanofoundation.conversions.domain.*;

public class PreviewEraHistoryFactory {

  public static List<EraHistoryItem> eraHistoryItems(GenesisPaths genesisPaths) {
    return List.of(
        new EraHistoryItem(
            Phase.Shelley,
            new Era(Shelley, Optional.of(genesisPaths.shelleyLink())),
            0L,
            Optional.empty(), // TODO find out this value based on some explorer data
            0L,
            Optional.of(55_814_400L - 1),
            0,
            Optional.of(646 - 1),
            VER_6_0,
            Optional.of(Praos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Voltaire,
            Era.noGenesis(EraType.Conway),
            -1L, // TODO find out this value based on some explorer data
            Optional
                .empty(), // TODO find out this value based on some explorer data when the era ends
            55_814_400L,
            Optional.empty(),
            646,
            Optional.empty(),
            VER_9_1,
            Optional.of(Praos),
            Ouroboros_Praos,
            true));
  }
}
