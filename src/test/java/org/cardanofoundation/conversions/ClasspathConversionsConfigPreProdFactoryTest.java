package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.PREPROD;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClasspathConversionsConfigPreProdFactoryTest {

  private static ConversionsConfig classpathConversionsConfigFactory;

  @BeforeAll
  static void setUp() {
    classpathConversionsConfigFactory = ClasspathConversionsConfigFactory.create(PREPROD);
  }

  @Test
  public void testByronGenesisClasspathFileReading() {
    assertThat(classpathConversionsConfigFactory.byronGenesisFile()).isNotNull();
  }

  @Test
  public void testShelleyGenesisClasspathFileReading() {
    assertThat(classpathConversionsConfigFactory.shelleyGenesisFile()).isNotNull();
  }
}
