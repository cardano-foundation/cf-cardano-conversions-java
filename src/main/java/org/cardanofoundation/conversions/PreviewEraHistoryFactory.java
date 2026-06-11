package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Consensus.Ouroboros_Praos;
import static org.cardanofoundation.conversions.domain.EraType.Alonzo;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.Praos;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.TPraos;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_6_0;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_7_0;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.VER_9_1;

import java.util.List;
import java.util.Optional;
import org.cardanofoundation.conversions.domain.*;

/**
 * Preview never ran Byron; it launched directly in Alonzo (ledger protocol v6, epochs 0-2),
 * hard-forked to Babbage at epoch 3 and to Conway at epoch 646 (verified against on-chain
 * epoch_params / block data).
 */
public class PreviewEraHistoryFactory {

  public static List<EraHistoryItem> eraHistoryItems(GenesisPaths genesisPaths) {
    return List.of(
        new EraHistoryItem(
            Phase.Gougen,
            new Era(Alonzo, Optional.of(genesisPaths.shelleyLink())),
            0L,
            Optional.of(259_180L),
            0L,
            Optional.of(259_200L - 1L),
            0,
            Optional.of(3 - 1),
            VER_6_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Babbage),
            259_215L,
            Optional.of(55_814_394L),
            259_200L,
            Optional.of(55_814_400L - 1L),
            3,
            Optional.of(646 - 1),
            VER_7_0,
            Optional.of(Praos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Voltaire,
            Era.noGenesis(EraType.Conway),
            55_814_440L,
            Optional.empty(), // Conway is the currently running era
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
