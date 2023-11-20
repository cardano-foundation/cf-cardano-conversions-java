// package org.cardanofoundation.conversions.domain;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.cardanofoundation.conversions.GenesisConfig;
//
// import static org.cardanofoundation.conversions.domain.Era.Byron;
//
// @RequiredArgsConstructor
// @Slf4j
// public class EraService {
//
//    private final GenesisConfig genesisConfig;
//
//    public int getShelleyEpochSlot(long shelleyAbsoluteSlot) {
//        var shelleyStartSlot = genesisConfig.getShelleyStartSlot();
//
//        if (shelleyStartSlot == -1) {
//            shelleyStartSlot = eraStorage.findFirstNonByronEra().map(cardanoEra ->
// cardanoEra.getStartSlot()) //For local devenet, it could be babbage era
//                    .orElseThrow(() -> new IllegalStateException("Shelley start slot not found"));
//        }
//
//        return epochConfig.shelleyEpochSlot(shelleyStartSlot, shelleyAbsoluteSlot);
//    }
//
//    public long shelleyEraStartTime() {
//        firstShelleySlot();
//
//        long startTime = genesisConfig.getStartTime(storeProperties.getProtocolMagic());
//        _shelleyStartTime = startTime + firstShelleySlot() *
// (long)genesisConfig.slotDuration(Era.Byron);
//
//        return _shelleyStartTime;
//    }
//
//    private long firstShelleySlot() {
//        // calculate Byron Era last slot time
//
//        if (_firstShelleySlot == 0) {
//            _firstShelleySlot = eraStorage.findFirstNonByronEra().map(cardanoEra ->
// cardanoEra.getStartSlot())
//                    .orElse(0L);
//        }
//
//        return _firstShelleySlot;
//    }
//
//    public long shelleyEraStartTime() {
//        if (_shelleyStartTime != 0)
//            return _shelleyStartTime;
//        firstShelleySlot();
//
//        long startTime = genesisConfig.getStartTime(storeProperties.getProtocolMagic());
//        _shelleyStartTime = startTime + firstShelleySlot() *
// (long)genesisConfig.slotDuration(Byron);
//
//        return _shelleyStartTime;
//    }
//
//    public long slotsPerEpoch(Era era) {
//        return genesisConfig.slotsPerEpoch(era);
//    }
//
//    public long blockTime(Era era,
//                          long slot) {
//        if (era == Byron) {
//            return byronBlockTime(slot);
//        }
//
//        long slotsFromShelleyStart = slot - firstShelleySlot();
//
//        return (shelleyEraStartTime() + slotsFromShelleyStart * (long)
// genesisConfig.slotDuration(Era.Shelley));
//    }
//
//    private long byronBlockTime(long slot) {
//        long startTime = genesisConfig.getStartTime(genesisConfig.getProtocolNetworkMagic());
//
//        return startTime + slot * (long) genesisConfig.slotDuration(Byron);
//    }
//
// }
