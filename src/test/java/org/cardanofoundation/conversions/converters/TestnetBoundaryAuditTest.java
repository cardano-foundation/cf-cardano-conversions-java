package org.cardanofoundation.conversions.converters;

import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;
import static org.cardanofoundation.conversions.domain.NetworkType.PREVIEW;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.Test;

/**
 * Same idea as {@link MainNetBoundaryAuditTest} but for preprod and preview.
 *
 * <p>Preprod ground truth: byron start 1654041600 (2022-06-01T00:00:00Z), Byron slots 0..86399 at
 * 20s (epochs 0..3), Shelley from slot 86400 at unix 1655769600 (2022-06-21T00:00:00Z), epoch 4,
 * 432000-slot epochs.
 *
 * <p>Preview ground truth: start 1666656000 (2022-10-25T00:00:00Z), no Byron, 1s slots, 86400-slot
 * epochs; slot == unix - start; epoch == slot / 86400.
 */
@Slf4j
class TestnetBoundaryAuditTest {

  static LocalDateTime utc(long unix) {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(unix), ZoneOffset.UTC);
  }

  @Test
  void auditPreProdAroundRecentBoundaries() {
    var converters = ClasspathConversionsFactory.createConverters(PREPROD);
    long shelleyStartUnix = 1655769600L;
    long firstShelleySlot = 86_400L;
    int firstShelleyEpoch = 4;
    long epochLen = 432_000L;

    List<String> failures = new ArrayList<>();
    // epoch 339 starts 2026-06-... recent; also Conway start 163 and a mid one
    int[] epochs = {5, 108, 163, 300, 339};

    for (int epochNo : epochs) {
      long boundary = shelleyStartUnix + (long) (epochNo - firstShelleyEpoch) * epochLen;
      for (long unix = boundary - 300; unix <= boundary + 300; unix++) {
        int expectedEpoch = firstShelleyEpoch + (int) ((unix - shelleyStartUnix) / epochLen);
        long expectedSlot = firstShelleySlot + (unix - shelleyStartUnix);

        int actualEpoch = converters.time().utcTimeToEpochNo(utc(unix));
        long actualSlot = converters.time().toSlot(utc(unix));
        long actualEpochFromSlot = converters.slot().slotToEpoch(expectedSlot);

        if (expectedEpoch != actualEpoch) {
          failures.add(
              "preprod utcTimeToEpochNo("
                  + utc(unix)
                  + ") = "
                  + actualEpoch
                  + ", expected "
                  + expectedEpoch
                  + " (offset "
                  + (unix - boundary)
                  + "s)");
        }
        if (expectedSlot != actualSlot) {
          failures.add(
              "preprod toSlot(" + utc(unix) + ") = " + actualSlot + ", expected " + expectedSlot);
        }
        if (expectedEpoch != actualEpochFromSlot) {
          failures.add(
              "preprod slotToEpoch("
                  + expectedSlot
                  + ") = "
                  + actualEpochFromSlot
                  + ", expected "
                  + expectedEpoch);
        }
      }

      long expectedFirstSlot = firstShelleySlot + (long) (epochNo - firstShelleyEpoch) * epochLen;
      long actualFirstSlot = converters.epoch().epochToAbsoluteSlot(epochNo, START);
      if (expectedFirstSlot != actualFirstSlot) {
        failures.add(
            "preprod epochToAbsoluteSlot("
                + epochNo
                + ", START) = "
                + actualFirstSlot
                + ", expected "
                + expectedFirstSlot);
      }
    }

    failures.stream().limit(30).forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches, first 30 logged");
  }

  @Test
  void auditPreviewAroundRecentBoundaries() {
    var converters = ClasspathConversionsFactory.createConverters(PREVIEW);
    long startUnix = 1666656000L;
    long epochLen = 86_400L;

    List<String> failures = new ArrayList<>();
    int[] epochs = {1, 646, 1000, 1325};

    for (int epochNo : epochs) {
      long boundary = startUnix + (long) epochNo * epochLen;
      for (long unix = boundary - 300; unix <= boundary + 300; unix++) {
        int expectedEpoch = (int) ((unix - startUnix) / epochLen);
        long expectedSlot = unix - startUnix;

        int actualEpoch = converters.time().utcTimeToEpochNo(utc(unix));
        long actualSlot = converters.time().toSlot(utc(unix));
        long actualEpochFromSlot = converters.slot().slotToEpoch(expectedSlot);

        if (expectedEpoch != actualEpoch) {
          failures.add(
              "preview utcTimeToEpochNo("
                  + utc(unix)
                  + ") = "
                  + actualEpoch
                  + ", expected "
                  + expectedEpoch
                  + " (offset "
                  + (unix - boundary)
                  + "s)");
        }
        if (expectedSlot != actualSlot) {
          failures.add(
              "preview toSlot(" + utc(unix) + ") = " + actualSlot + ", expected " + expectedSlot);
        }
        if (expectedEpoch != actualEpochFromSlot) {
          failures.add(
              "preview slotToEpoch("
                  + expectedSlot
                  + ") = "
                  + actualEpochFromSlot
                  + ", expected "
                  + expectedEpoch);
        }
      }

      long expectedFirstSlot = (long) epochNo * epochLen;
      long actualFirstSlot = converters.epoch().epochToAbsoluteSlot(epochNo, START);
      if (expectedFirstSlot != actualFirstSlot) {
        failures.add(
            "preview epochToAbsoluteSlot("
                + epochNo
                + ", START) = "
                + actualFirstSlot
                + ", expected "
                + expectedFirstSlot);
      }
    }

    failures.stream().limit(30).forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches, first 30 logged");
  }
}
