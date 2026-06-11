package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;
import static org.cardanofoundation.conversions.domain.NetworkType.PREVIEW;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.domain.EraHistoryItem;
import org.cardanofoundation.conversions.domain.NetworkType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Validates the hand-maintained era history tables against the conversion arithmetic itself:
 *
 * <ul>
 *   <li>items are sorted, contiguous in epochs and theoretical slots
 *   <li>firstTheoreticalSlotNo / lastTheoreticalSlotNo agree with epoch arithmetic
 *   <li>real slots fall inside theoretical bounds
 * </ul>
 */
@Slf4j
class EraHistoryConsistencyTest {

  @ParameterizedTest
  @EnumSource(
      value = NetworkType.class,
      names = {"MAINNET", "PREPROD", "PREVIEW"})
  void eraHistoryIsContiguousAndMatchesEpochArithmetic(NetworkType networkType) {
    var converters = ClasspathConversionsFactory.createConverters(networkType);
    var epochConversions = converters.epoch();
    List<EraHistoryItem> items = converters.genesisConfig().getEraHistory().all();

    List<String> failures = new ArrayList<>();

    for (int i = 0; i < items.size(); i++) {
      EraHistoryItem item = items.get(i);
      String id = networkType + "/" + item.era().eraType() + "[" + i + "]";

      // theoretical first slot must equal the first slot of the start epoch
      long expectedFirst = epochConversions.beginningOfEpochToAbsoluteSlot(item.startEpochNo());
      if (item.firstTheoreticalSlotNo() != expectedFirst) {
        failures.add(
            id
                + ": firstTheoreticalSlotNo="
                + item.firstTheoreticalSlotNo()
                + ", expected "
                + expectedFirst
                + " (start of epoch "
                + item.startEpochNo()
                + ")");
      }

      // theoretical last slot must equal the last slot of the end epoch
      if (item.endEpochNo().isPresent()) {
        long expectedLast = epochConversions.endingOfEpochToAbsoluteSlot(item.endEpochNo().get());
        if (item.lastTheoreticalSlotNo().isEmpty()) {
          failures.add(
              id
                  + ": endEpochNo="
                  + item.endEpochNo().get()
                  + " present but lastTheoreticalSlotNo empty, expected "
                  + expectedLast);
        } else if (item.lastTheoreticalSlotNo().get() != expectedLast) {
          failures.add(
              id
                  + ": lastTheoreticalSlotNo="
                  + item.lastTheoreticalSlotNo().get()
                  + ", expected "
                  + expectedLast
                  + " (end of epoch "
                  + item.endEpochNo().get()
                  + ")");
        }
      }

      // real slots must fall within theoretical bounds (-1 is a documented "unknown" placeholder)
      if (item.firstRealSlotNo() >= 0 && item.firstRealSlotNo() < item.firstTheoreticalSlotNo()) {
        failures.add(
            id
                + ": firstRealSlotNo="
                + item.firstRealSlotNo()
                + " < firstTheoreticalSlotNo="
                + item.firstTheoreticalSlotNo());
      }
      if (item.lastRealSlotNo().isPresent()
          && item.lastTheoreticalSlotNo().isPresent()
          && item.lastRealSlotNo().get() > item.lastTheoreticalSlotNo().get()) {
        failures.add(
            id
                + ": lastRealSlotNo="
                + item.lastRealSlotNo().get()
                + " > lastTheoreticalSlotNo="
                + item.lastTheoreticalSlotNo().get());
      }

      // contiguity with next item
      if (i + 1 < items.size()) {
        EraHistoryItem next = items.get(i + 1);
        if (item.endEpochNo().isEmpty()) {
          failures.add(id + ": endEpochNo empty but a later era history item exists");
        } else if (next.startEpochNo() != item.endEpochNo().get() + 1) {
          failures.add(
              id
                  + ": endEpochNo="
                  + item.endEpochNo().get()
                  + " not contiguous with next item's"
                  + " startEpochNo="
                  + next.startEpochNo());
        }
        if (item.lastTheoreticalSlotNo().isPresent()
            && next.firstTheoreticalSlotNo() != item.lastTheoreticalSlotNo().get() + 1) {
          failures.add(
              id
                  + ": lastTheoreticalSlotNo="
                  + item.lastTheoreticalSlotNo().get()
                  + " not contiguous with next item's firstTheoreticalSlotNo="
                  + next.firstTheoreticalSlotNo());
        }
      }
    }

    failures.forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), networkType + ": " + failures.size() + " inconsistencies");
  }

  /** Only the last item of each table should be flagged as the currently running era. */
  @Test
  void onlyLastItemIsRunning() {
    for (NetworkType networkType : new NetworkType[] {MAINNET, PREPROD, PREVIEW}) {
      var items =
          ClasspathConversionsFactory.createConverters(networkType)
              .genesisConfig()
              .getEraHistory()
              .all();

      for (int i = 0; i < items.size(); i++) {
        boolean expected = (i == items.size() - 1);
        org.junit.jupiter.api.Assertions.assertEquals(
            expected,
            items.get(i).isRunning(),
            networkType + " item " + i + " (" + items.get(i).era().eraType() + ") isRunning");
      }
    }
  }
}
