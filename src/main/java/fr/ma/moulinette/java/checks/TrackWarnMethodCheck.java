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
 * Implémentation de règle SonarQube custom pour tracer l'utilisation de LOGGER.warn().
 */
@Rule(
  key = "track-warn-method",
  priority = Priority.INFO,
  name = "Utilisation de LOGGER.warn.",
  description = "Cette règle vérifie l'utilisation de la méthode LOGGER.warn.",
  tags = {"track", "logger", "ma-moulinette"}
)
public class TrackWarnMethodCheck extends AbstractTrackLoggerMethodCheck {

  /**
   * Spécifie le nom de méthode à tracer (warn ici).
   *
   * @return le nom de méthode "warn".
   */
  @Override
  protected String getMethodName() {
    return "warn";
  }

  /**
   * Spécifie le message de logger pour le niveau warn.
   *
   * @return le message "Utilisation du Logger en mode : warn".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : warn";
  }
}
