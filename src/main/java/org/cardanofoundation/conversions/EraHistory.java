package org.cardanofoundation.conversions;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.conversions.domain.EraHistoryItem;
import org.cardanofoundation.conversions.domain.EraType;

/**
 * Data sources: - https://cips.cardano.org/cips/cip59/feature-table.md.html - -
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

  /**
   * Returns the last era history item of a given era. An era can span multiple items (one per
   * intra-era protocol version bump), so questions about the end of an era must be answered from
   * the last item, not the first one.
   */
  public Optional<EraHistoryItem> findLastByEra(EraType eraType) {
    return all().stream()
        .filter(eraHistoryItem -> eraHistoryItem.era().eraType() == eraType)
        .reduce((first, second) -> second);
  }
}
