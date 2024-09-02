package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.Consensus.*;
import static org.cardanofoundation.conversions.domain.EraType.Byron;
import static org.cardanofoundation.conversions.domain.EraType.Shelley;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.Praos;
import static org.cardanofoundation.conversions.domain.LedgerProtocol.TPraos;
import static org.cardanofoundation.conversions.domain.ProtocolVersion.*;

import java.util.List;
import java.util.Optional;
import org.cardanofoundation.conversions.domain.*;

public class MainNetEraHistoryFactory {

  public static List<EraHistoryItem> eraHistoryItems(GenesisPaths genesisPaths) {
    return List.of(
        new EraHistoryItem(
            Phase.Byron,
            new Era(Byron, Optional.of(genesisPaths.byronLink())),
            0L,
            Optional.empty(),
            0L,
            Optional.of(3_801_600L - 1L),
            0,
            Optional.of(175),
            VER_0_0,
            Optional.empty(),
            Ouroboros_Classic,
            false),
        new EraHistoryItem(
            Phase.Byron,
            new Era(Byron, Optional.of(genesisPaths.byronLink())),
            3801600L,
            Optional.of(4_492_799L),
            3_801_600L,
            Optional.of(4_492_800L - 1L),
            176,
            Optional.of(207),
            ProtocolVersion.VER_1_0,
            Optional.empty(),
            Ouroboros_BFT,
            false),
        new EraHistoryItem(
            Phase.Shelley,
            new Era(Shelley, Optional.of(genesisPaths.shelleyLink())),
            4_492_800L,
            Optional.of(16_588_737L),
            4_492_800L,
            Optional.of(16_588_800L - 1L),
            208,
            Optional.of(235),
            ProtocolVersion.VER_2_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Allegra),
            16_588_800L,
            Optional.of(23_068_793L),
            16_588_800L,
            Optional.of(23_068_800L - 1L),
            236,
            Optional.of(250),
            ProtocolVersion.VER_3_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Mary),
            23_068_800L,
            Optional.of(399_167_96L),
            23_068_800L,
            Optional.of(39_916_800L),
            251,
            Optional.of(289),
            ProtocolVersion.VER_4_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Alonzo),
            39_916_975L,
            Optional.empty(), // TODO: find out this value based on some explorer data
            39_916_800L,
            Optional.of(43_372_800L - 1L),
            290,
            Optional.of(297),
            ProtocolVersion.VER_5_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Alonzo),
            43_372_972L,
            Optional.of(72_316_796L),
            43_372_800L,
            Optional.of(72_316_800L - 1L),
            298,
            Optional.of(364),
            VER_6_0,
            Optional.of(TPraos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Babbage),
            72_316_896L,
            Optional.empty(), // TODO: find out this value based on some explorer data
            72_316_800L,
            Optional.empty(),
            365,
            Optional.of(393),
            ProtocolVersion.VER_7_0,
            Optional.of(Praos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Gougen,
            Era.noGenesis(EraType.Babbage),
            84_844_885L,
            Optional.of(133_660_799L), // https://cardanoscan.io/block/10781330
            84_844_800L,
            Optional.of(108_172_800L - 1L),
            394,
            Optional.of(507 - 1),
            VER_8_0,
            Optional.of(Praos),
            Ouroboros_Praos,
            false),
        new EraHistoryItem(
            Phase.Voltaire,
            Era.noGenesis(EraType.Conway),
            133_660_855L, // https://cardanoscan.io/block/10781331
            Optional
                .empty(), // Conway era is an active era soon, and we don't know when it ends yet
            133_660_800L,
            Optional
                .empty(), // Conway era is an active era soon, and we don't know when it ends yet
            507,
            Optional.empty(),
            VER_9_1,
            Optional.of(Praos),
            Ouroboros_Praos,
            true));
  }
}
