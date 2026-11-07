/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  SPDX-License-Identifier: LGPL-3.0-or-later
 *  Voir le fichier LICENSE à la racine du dépôt.
 */
package fr.ma.moulinette.java;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarProduct;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Vérifie que {@link MyJavaPlugin} enregistre exactement les extensions attendues.
 *
 * Extensions originales :
 *   - MyJavaRulesDefinition
 *   - MyJavaFileCheckRegistrar
 *   - MyJavaQualityProfile
 *
 * Extensions ajoutées pour les métriques par framework :
 *   - LoggerMetrics       (déclaration des métriques custom)
 *   - LoggerCounter       (compteur partagé @ScannerSide)
 *   - LoggerMetricsSensor (publication des métriques)
 */
class MyJavaPluginTest {

  @Test
  void testName() {
    Plugin.Context context = new Plugin.Context(new MockedSonarRuntime());

    new MyJavaPlugin().define(context);

    assertThat(context.getExtensions())
      .extracting(ext -> ((Class<?>) ext).getSimpleName())
      .containsExactlyInAnyOrder(
        // Extensions originales
        "MyJavaRulesDefinition",
        "MyJavaFileCheckRegistrar",
        "MyJavaQualityProfile",
        // Extensions métriques par framework
        "LoggerMetrics",
        "LoggerCounter",
        "LoggerMetricsSensor");
  }

  public static class MockedSonarRuntime implements SonarRuntime {

    @Override
    public Version getApiVersion() {
      // Aligné sur sonarjava 8.2.0 / SonarQube 10.7 LTS
      return Version.create(10, 7);
    }

    @Override
    public SonarProduct getProduct() {
      return SonarProduct.SONARQUBE;
    }

    @Override
    public SonarQubeSide getSonarQubeSide() {
      return SonarQubeSide.SCANNER;
    }

    @Override
    public SonarEdition getEdition() {
      return SonarEdition.COMMUNITY;
    }
  }
}
