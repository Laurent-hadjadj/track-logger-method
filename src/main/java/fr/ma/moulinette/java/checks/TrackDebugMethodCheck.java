/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  SPDX-License-Identifier: LGPL-3.0-or-later
 *  Voir le fichier LICENSE à la racine du dépôt.
 */
package fr.ma.moulinette.java.checks;

import org.sonar.check.Rule;
import org.sonar.check.Priority;

/**
 * Implémentation de règle SonarQube custom pour tracer l'utilisation de LOGGER.debug().
 */
@Rule(
  key = "track-debug-method",
  priority = Priority.INFO,
  name = "Utilisation de LOGGER.debug.",
  description = "Cette règle vérifie l'utilisation de la méthode LOGGER.debug.",
  tags = {"track", "logger", "ma-moulinette"}
)
public class TrackDebugMethodCheck extends AbstractTrackLoggerMethodCheck {

  /**
   * Spécifie le nom de méthode à tracer (debug ici).
   *
   * @return le nom de méthode "debug".
   */
  @Override
  protected String getMethodName() {
    return "debug";
  }

  /**
   * Spécifie le message de logger pour le niveau debug.
   *
   * @return le message "Utilisation du Logger en mode : debug".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : debug";
  }
}
