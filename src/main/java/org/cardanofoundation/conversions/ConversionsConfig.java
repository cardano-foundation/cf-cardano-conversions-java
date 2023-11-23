package org.cardanofoundation.conversions;

import java.net.URL;
import org.cardanofoundation.conversions.domain.NetworkType;

public record ConversionsConfig(
    NetworkType networkType, URL byronGenesisFile, URL shelleyGenesisFile) {}
