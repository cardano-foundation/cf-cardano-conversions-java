package org.cardanofoundation.conversions;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.domain.EraHistoryItem;
import org.cardanofoundation.conversions.domain.EraType;

/**
 * Data sources: - https://cips.cardano.org/cips/cip59/feature-table.md.html -
 * https://cardanosolutions.github.io/kupo/#section/Rollbacks-and-chain-forks/How-Kupo-deals-with-rollbacks
 */
@RequiredArgsConstructor
public class EraHistory {

  private final List<EraHistoryItem> eraHistoryItems;

  public List<EraHistoryItem> all() {
    return eraHistoryItems;
  }

  public Optional<EraHistoryItem> findFirstByEra(EraType eraType) {
    return all().stream()
        .filter(eraHistoryItem -> eraHistoryItem.era().eraType() == eraType)
        .findFirst();
  }
}
