package org.cardanofoundation.conversions.domain;

public record EraLine(
    Phase phase,
    Era era,
    long slotNo,
    ProtocolVersion protocolVersion,
    LedgerProtocol ledgerProtocol,
    Consensus consensus) {}
