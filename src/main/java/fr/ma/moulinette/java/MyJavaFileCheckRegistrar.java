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

import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonarsource.api.sonarlint.SonarLintSide;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Enregistre les classes de check pour l'analyse, contre le repository de règles du plugin.
 *
 * Aucune injection de compteur n'est nécessaire ici car LoggerCounter expose une API statique
 * (LoggerCounter.increment()) que les checks peuvent appeler directement sans référence à
 * l'instance.
 */
@SonarLintSide
public class MyJavaFileCheckRegistrar implements CheckRegistrar {

  /**
   * Enregistre les classes de check contre la clé du repository.
   *
   * @param registrarContext le contexte d'enregistrement SonarQube
   */
  @Override
  public void register(@Nonnull RegistrarContext registrarContext) {
    registrarContext.registerClassesForRepository(
        MyJavaRulesDefinition.REPOSITORY_KEY,
        checkClasses(),
        testCheckClasses());
  }

  /** Checks principaux fournis par le plugin. */
  public static List<Class<? extends JavaCheck>> checkClasses() {
    return RulesList.getJavaChecks();
  }

  /** Checks de test fournis par le plugin (aucun défini). */
  public static List<Class<? extends JavaCheck>> testCheckClasses() {
    return RulesList.getJavaTestChecks();
  }
}
