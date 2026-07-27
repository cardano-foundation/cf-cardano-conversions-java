package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Consensus.Ouroboros_BFT;
import static org.cardanofoundation.conversions.domain.Consensus.Ouroboros_Praos;
import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.Praos;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.TPraos;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.*;

import java.util.List;
import java.util.Optional;
import org.cardanofoundation.conversions.domain.*;

public class PreProdEraHistoryFactory {

  // real first/last block slots verified against on-chain data (Koios), 2026-06
  public static List<EraHistoryItem> eraHistoryItems(GenesisPaths genesisPaths) {
    return List.of(
        new EraHistoryItem(
            Phase.Byron,
            new Era(Byron, Optional.of(genesisPaths.byronLink())),
            0,
            Optional.of(84_242L),
            0,
            Optional.of(86_400L - 1L),
            0,
            Optional.of(3),
            ProtocolVersion.VER_1_0,
            Optional.empty(),
            Ouroboros_BFT,
            false),
        new EraHistoryItem(
            Phase.Shelley,
            new Era(Shelley, Optional.of(genesisPaths.shelleyLink())),
            86_400L,
            Optional.of(518_360L),
            86_400L,
            Optional.of(518_400L - 1L),
            4,
            Optional.of(4),
            ProtocolVersion.VER_2_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Allegra),
            518_400L,
            Optional.of(950_340L),
            518_400L,
            Optional.of(950_400L - 1L),
            5,
            Optional.of(5),
            ProtocolVersion.VER_3_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Mary),
            950_410L,
            Optional.of(1_382_348L),
            950_400L,
            Optional.of(1_382_400L - 1L),
            6,
            Optional.of(6),
            ProtocolVersion.VER_4_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Alonzo),
            1_382_422L,
            Optional.of(3_931_295L),
            1_382_400L,
            Optional.of(3_974_400L - 1L),
            7,
            Optional.of(12),
            VER_6_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Babbage),
            3_974_409L,
            Optional.of(68_774_372L),
            3_974_400L,
            Optional.of(68_774_400L - 1L),
            13,
            Optional.of(163 - 1),
            VER_8_0,
            Optional.of(Praos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Voltaire,
            Era.noGenesis(EraType.Conway),
            68_774_413L,
            Optional.empty(),
            68_774_400L,
            Optional.empty(),
            163,
            Optional.empty(),
            VER_9_1,
            Optional.of(Praos),
            Ouroboros_Praos,
            true));
  }
}
