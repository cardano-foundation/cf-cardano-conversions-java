package org.cardanofoundation.conversions;

import org.cardanofoundation.conversions.domain.GenesisPaths;
import org.cardanofoundation.conversions.domain.NetworkType;

public record ConversionsConfig(
    NetworkType networkType, GenesisPaths genesisPaths, EraHistory eraHistory) {}
