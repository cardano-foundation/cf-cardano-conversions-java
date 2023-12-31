package org.cardanofoundation.conversions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cardanofoundation.conversions.domain.NetworkType.MAINNET;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ClasspathConversionsConfigMainNetFactoryTest {

  private static ConversionsConfig classpathConversionsConfigFactory;

  @BeforeAll
  static void setUp() {
    classpathConversionsConfigFactory = ClasspathConversionsFactory.create(MAINNET);
  }

  @Test
  public void testByronGenesisClasspathFileReading() {
    assertThat(classpathConversionsConfigFactory.genesisPaths().byronLink()).isNotNull();
  }

  @Test
  public void testShelleyGenesisClasspathFileReading() {
    assertThat(classpathConversionsConfigFactory.genesisPaths().shelleyLink()).isNotNull();
  }
}
