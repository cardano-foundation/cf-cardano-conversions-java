package org.cardanofoundation.conversions.converters;

import static org.cardanofoundation.conversions.domain.EpochOffset.END;
import static org.cardanofoundation.conversions.domain.EpochOffset.START;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cardanofoundation.conversions.ClasspathConversionsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Audit harness: compares every conversion function against pure ground-truth arithmetic derived
 * from well-known mainnet constants, scanning around epoch boundaries (including recent epochs).
 *
 * <p>Ground truth (mainnet): system start 1506203091 (2017-09-23T21:44:51Z); Byron slots 0..4492799
 * at 20s each; Shelley+ from slot 4492800 (unix 1596059091 = 2020-07-29T21:44:51Z) at 1s slots;
 * epochs are 21600 Byron slots / 432000 Shelley slots; Shelley starts at epoch 208.
 */
@Slf4j
class MainNetBoundaryAuditTest {

  static final long SYSTEM_START = 1506203091L;
  static final long SHELLEY_START_UNIX = 1596059091L;
  static final long FIRST_SHELLEY_SLOT = 4_492_800L;
  static final int FIRST_SHELLEY_EPOCH = 208;
  static final long SHELLEY_EPOCH_LEN = 432_000L;

  private CardanoConvertersHolder holder;

  record CardanoConvertersHolder(
      EpochConversions epoch, SlotConversions slot, TimeConversions time) {}

  @BeforeEach
  void setup() {
    var converters = ClasspathConversionsFactory.createConverters(MAINNET);
    holder = new CardanoConvertersHolder(converters.epoch(), converters.slot(), converters.time());
  }

  // ---- ground truth helpers ----

  static long truthEpochStartUnix(int epochNo) {
    if (epochNo >= FIRST_SHELLEY_EPOCH) {
      return SHELLEY_START_UNIX + (long) (epochNo - FIRST_SHELLEY_EPOCH) * SHELLEY_EPOCH_LEN;
    }
    return SYSTEM_START + (long) epochNo * 21600L * 20L;
  }

  static int truthEpochAtUnix(long unix) {
    if (unix >= SHELLEY_START_UNIX) {
      return FIRST_SHELLEY_EPOCH + (int) ((unix - SHELLEY_START_UNIX) / SHELLEY_EPOCH_LEN);
    }
    return (int) ((unix - SYSTEM_START) / (21600L * 20L));
  }

  static long truthSlotAtUnix(long unix) {
    if (unix >= SHELLEY_START_UNIX) {
      return FIRST_SHELLEY_SLOT + (unix - SHELLEY_START_UNIX);
    }
    return (unix - SYSTEM_START) / 20L;
  }

  static int truthEpochOfSlot(long slot) {
    if (slot >= FIRST_SHELLEY_SLOT) {
      return FIRST_SHELLEY_EPOCH + (int) ((slot - FIRST_SHELLEY_SLOT) / SHELLEY_EPOCH_LEN);
    }
    return (int) (slot / 21600L);
  }

  static long truthSlotStartUnix(long slot) {
    if (slot >= FIRST_SHELLEY_SLOT) {
      return SHELLEY_START_UNIX + (slot - FIRST_SHELLEY_SLOT);
    }
    return SYSTEM_START + slot * 20L;
  }

  static LocalDateTime utc(long unix) {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(unix), ZoneOffset.UTC);
  }

  // ---- audits ----

  @Test
  void auditUtcTimeToEpochNoAroundRecentBoundaries() {
    List<String> failures = new ArrayList<>();
    // recent epochs (2025-2026) + Shelley start + a couple historical ones
    int[] epochs = {209, 300, 450, 507, 600, 630, 635, 636, 637};

    for (int epochNo : epochs) {
      long boundary = truthEpochStartUnix(epochNo); // first second of epochNo
      for (long unix = boundary - 600; unix <= boundary + 600; unix++) {
        int expected = truthEpochAtUnix(unix);
        int actual = holder.time().utcTimeToEpochNo(utc(unix));
        if (expected != actual) {
          failures.add(
              "utcTimeToEpochNo("
                  + utc(unix)
                  + ") = "
                  + actual
                  + ", expected "
                  + expected
                  + " (boundary of epoch "
                  + epochNo
                  + " at "
                  + utc(boundary)
                  + ", offset "
                  + (unix - boundary)
                  + "s)");
        }
      }
    }

    failures.stream().limit(50).forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches, first 50 logged");
  }

  @Test
  void auditUtcTimeToEpochNoWithNanos() {
    List<String> failures = new ArrayList<>();
    long boundary = truthEpochStartUnix(636);

    for (long unix = boundary - 5; unix <= boundary + 5; unix++) {
      for (int nano : new int[] {0, 1, 500_000_000, 999_999_999}) {
        int expected = truthEpochAtUnix(unix);
        int actual = holder.time().utcTimeToEpochNo(utc(unix).withNano(nano));
        if (expected != actual) {
          failures.add(
              "utcTimeToEpochNo("
                  + utc(unix).withNano(nano)
                  + ") = "
                  + actual
                  + ", expected "
                  + expected);
        }
      }
    }

    failures.forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches");
  }

  @Test
  void auditToSlotAroundBoundaries() {
    List<String> failures = new ArrayList<>();
    int[] epochs = {209, 450, 507, 636};

    for (int epochNo : epochs) {
      long boundary = truthEpochStartUnix(epochNo);
      for (long unix = boundary - 120; unix <= boundary + 120; unix++) {
        long expected = truthSlotAtUnix(unix);
        long actual = holder.time().toSlot(utc(unix));
        if (expected != actual) {
          failures.add("toSlot(" + utc(unix) + ") = " + actual + ", expected " + expected);
        }
      }
    }

    failures.stream().limit(20).forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches");
  }

  @Test
  void auditSlotToEpochAroundBoundaries() {
    List<String> failures = new ArrayList<>();
    int[] epochs = {1, 207, 208, 209, 450, 507, 636};

    for (int epochNo : epochs) {
      long firstSlot =
          epochNo >= FIRST_SHELLEY_EPOCH
              ? FIRST_SHELLEY_SLOT + (long) (epochNo - FIRST_SHELLEY_EPOCH) * SHELLEY_EPOCH_LEN
              : epochNo * 21600L;
      for (long slot = firstSlot - 50; slot <= firstSlot + 50; slot++) {
        if (slot < 0) continue;
        long expected = truthEpochOfSlot(slot);
        long actual = holder.slot().slotToEpoch(slot);
        if (expected != actual) {
          failures.add("slotToEpoch(" + slot + ") = " + actual + ", expected " + expected);
        }
      }
    }

    failures.stream().limit(20).forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches");
  }

  @Test
  void auditSlotToTimeAroundBoundaries() {
    List<String> failures = new ArrayList<>();
    long[] slots = {0, 21599, 21600, 4492799, 4492800, 4492801, 109090938, 189000000};

    for (long slot : slots) {
      var expected = utc(truthSlotStartUnix(slot));
      var actual = holder.slot().slotToTime(slot);
      if (!expected.equals(actual)) {
        failures.add("slotToTime(" + slot + ") = " + actual + ", expected " + expected);
      }
    }

    failures.forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches");
  }

  @Test
  void auditEpochToAbsoluteSlotAndTime() {
    List<String> failures = new ArrayList<>();

    for (int epochNo = 0; epochNo <= 700; epochNo++) {
      long expectedFirst =
          epochNo >= FIRST_SHELLEY_EPOCH
              ? FIRST_SHELLEY_SLOT + (long) (epochNo - FIRST_SHELLEY_EPOCH) * SHELLEY_EPOCH_LEN
              : epochNo * 21600L;
      long expectedLast =
          epochNo >= FIRST_SHELLEY_EPOCH
              ? expectedFirst + SHELLEY_EPOCH_LEN - 1
              : expectedFirst + 21600L - 1;

      long actualFirst = holder.epoch().epochToAbsoluteSlot(epochNo, START);
      long actualLast = holder.epoch().epochToAbsoluteSlot(epochNo, END);

      if (actualFirst != expectedFirst) {
        failures.add(
            "epochToAbsoluteSlot("
                + epochNo
                + ", START) = "
                + actualFirst
                + ", expected "
                + expectedFirst);
      }
      if (actualLast != expectedLast) {
        failures.add(
            "epochToAbsoluteSlot("
                + epochNo
                + ", END) = "
                + actualLast
                + ", expected "
                + expectedLast);
      }

      var expectedStartTime = utc(truthEpochStartUnix(epochNo));
      var actualStartTime = holder.epoch().epochToUTCTime(epochNo, START);
      if (!expectedStartTime.equals(actualStartTime)) {
        failures.add(
            "epochToUTCTime("
                + epochNo
                + ", START) = "
                + actualStartTime
                + ", expected "
                + expectedStartTime);
      }
    }

    failures.stream().limit(30).forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches");
  }

  /** Walks time forward over a boundary and asserts the reported epoch never decreases. */
  @Test
  void auditMonotonicityOverBoundary() {
    List<String> failures = new ArrayList<>();
    long boundary = truthEpochStartUnix(636);

    int prev = Integer.MIN_VALUE;
    for (long unix = boundary - 900; unix <= boundary + 900; unix++) {
      int cur = holder.time().utcTimeToEpochNo(utc(unix));
      if (cur < prev) {
        failures.add(
            "non-monotonic: epoch went from "
                + prev
                + " to "
                + cur
                + " at "
                + utc(unix)
                + " (offset "
                + (unix - boundary)
                + "s from epoch 636 start)");
      }
      prev = cur;
    }

    failures.forEach(log::error);
    org.junit.jupiter.api.Assertions.assertTrue(
        failures.isEmpty(), failures.size() + " mismatches");
  }
}
