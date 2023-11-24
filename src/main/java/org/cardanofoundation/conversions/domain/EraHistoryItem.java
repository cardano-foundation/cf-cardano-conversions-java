package org.cardanofoundation.conversions.domain;

import java.util.Optional;

public record EraHistoryItem(
    Phase phase,
    Era era,
    long firstRealSlotNo,
    Optional<Long> lastRealSlotNo,
    long firstTheoreticalSlotNo,
    Optional<Long> lastTheoreticalSlotNo,
    int startEpochNo,
    Optional<Integer> endEpochNo,
    ProtocolVersion protocolVersion,
    Optional<LedgerProtocol> ledgerProtocol,
    Consensus consensus,
    boolean isRunning) {}
