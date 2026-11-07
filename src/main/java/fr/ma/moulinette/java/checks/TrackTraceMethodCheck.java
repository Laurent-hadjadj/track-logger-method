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
 * Implémentation de règle SonarQube custom pour tracer l'utilisation de LOGGER.trace().
 */
@Rule(
  key = "track-trace-method",
  priority = Priority.INFO,
  name = "Utilisation de LOGGER.trace.",
  description = "Cette règle vérifie l'utilisation de la méthode LOGGER.trace.",
  tags = {"track", "logger", "ma-moulinette"}
)
public class TrackTraceMethodCheck extends AbstractTrackLoggerMethodCheck {

  /**
   * Spécifie le nom de méthode à tracer (trace ici).
   *
   * @return le nom de méthode "trace".
   */
  @Override
  protected String getMethodName() {
    return "trace";
  }

  /**
   * Spécifie le message de logger pour le niveau trace.
   *
   * @return le message "Utilisation du Logger en mode : trace".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : trace";
  }
}
