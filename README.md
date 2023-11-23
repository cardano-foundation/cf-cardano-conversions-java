# Cardano Conversions Utility

[![Build](https://github.com/cardano-foundation/cf-cardano-conversions-java/actions/workflows/tests.yaml/badge.svg)](https://github.com/cardano-foundation/cf-cardano-conversions-java/actions/workflows/tests.yaml)
[![License](https://img.shields.io:/github/license/cardano-foundation/cf-cardano-conversions-java?label=license)](https://github.com/cardano-foundation/cf-cardano-conversions-java/blob/master/LICENSE)
![Discord](https://img.shields.io/discord/1022471509173882950)

## Motivation

In Cardano, as a developer, it is common to find in various projects that there is a need to convert between various metrics, eg. start epoch to utc time, or slot number to epoch number or yet another metric.

This library aims to provide a common place for such conversions.

## Requirements
- JDK (>= 17)
- maven (>= 3)

## Cloning
```shell
git clone https://github.com/cardano-foundation/cf-cardano-conversions-java
```

## Maven / Gradle
Maven:
```xml
<dependency>
    <groupId>org.cardanofoundation</groupId>
    <artifactId>cf-cardano-conversions-java</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```
Gradle:
```
implementation("org.cardanofoundation:cf-cardano-conversions-java:0.1")
```

Example usages:
```java
var converters = ClasspathConversionsFactory.createConverters(NetworkType.MAINNET);

var epochStartTime = converters.epochConversions().epochToUTCTime(445, EpochOffset.START);
var utcTime = converters.slotConversions().slotToTime(109090938L);
var epochNo = converters.timeConversions().utcTimeToEpochNo(LocalDateTime.of(2023, 11, 22, 9, 48, 58));
var lastAlonzoAbsoluteSlot = converters.epochConversions().epochToAbsoluteSlot(364, EpochOffset.END);

System.out.println(epochStartTime); // LocalDateTime.of(2023, 10, 27, 21, 44, 51)
System.out.println(utcTime); // LocalDateTime.of(2023, 11, 22, 12, 47, 9)
System.out.println(epochNo); // 450
System.out.println(lastAlonzoAbsoluteSlot); // 72316799L
```

## Additional Docs
- [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
- [SECURITY.md](SECURITY.md)
- [CONTRIBUTING.md](CONTRIBUTING.md)
- [CHANGELOG.md](CHANGELOG.md)

## Local Building
```shell
cd cf-cardano-conversions-java
./mvnw clean install
```
