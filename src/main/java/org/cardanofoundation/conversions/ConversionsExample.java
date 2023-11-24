package org.cardanofoundation.conversions;

import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

public class ConversionsExample {

  public static void main(String[] args) {
    var converters = ClasspathConversionsFactory.createConverters(PREPROD);

    converters
        .genesisConfig()
        .getEraHistory()
        .all()
        .forEach(
            eraLine -> {
              System.out.println("Era:" + eraLine.era().eraType());

              System.out.println("Start Epoch No:" + eraLine.startEpochNo());
              System.out.println("End Epoch No:" + eraLine.endEpochNo());
              System.out.println("Real Epoch End Slot:" + eraLine.lastRealSlotNo());

              System.out.println(
                  "Start Absolute Slot:"
                      + converters.epoch().beginningOfEpochToAbsoluteSlot(eraLine.startEpochNo()));

              eraLine
                  .endEpochNo()
                  .ifPresent(
                      endEpochNo -> {
                        System.out.println(
                            "End Absolute Slot:"
                                + converters.epoch().endingOfEpochToAbsoluteSlot(endEpochNo));
                      });

              System.out.println("--------------------");
            });
  }
}
