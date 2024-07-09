/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2024.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  Licensed Creative Common  CC-BY-NC-SA 4.0.
 *  ---
 *  Vous pouvez obtenir une copie de la licence à l'adresse suivante :
 *  http://creativecommons.org/licenses/by-nc-sa/4.0/
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
 * [Description MyJavaRulesPluginTest]
 */
class MyJavaRulesPluginTest {

  @Test
  void testName() {
    Plugin.Context context = new Plugin.Context(new MockedSonarRuntime());

    new MyJavaRulesPlugin().define(context);

    assertThat(context.getExtensions())
      .extracting(ext -> ((Class) ext).getSimpleName())
      .containsExactlyInAnyOrder(
        "MyJavaRulesDefinition",
        "MyJavaFileCheckRegistrar");
  }

  public static class MockedSonarRuntime implements SonarRuntime {

    @Override
    public Version getApiVersion() {
      return Version.create(9, 9);
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
