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
 * Implémentation de règle SonarQube custom pour tracer l'utilisation de LOGGER.error().
 */
@Rule(
  key = "track-error-method",
  priority = Priority.INFO,
  name = "Utilisation de LOGGER.error.",
  description = "Cette règle vérifie l'utilisation de la méthode LOGGER.error.",
  tags = {"track", "logger", "ma-moulinette"}
)
public class TrackErrorMethodCheck extends AbstractTrackLoggerMethodCheck {

  /**
   * Spécifie le nom de méthode à tracer (error ici).
   *
   * @return le nom de méthode "error".
   */
  @Override
  protected String getMethodName() {
    return "error";
  }

  /**
   * Spécifie le message de logger pour le niveau error.
   *
   * @return le message "Utilisation du Logger en mode : error".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : error";
  }
}
