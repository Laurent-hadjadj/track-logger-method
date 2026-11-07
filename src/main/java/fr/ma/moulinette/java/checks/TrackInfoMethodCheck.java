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
 * Implémentation de règle SonarQube custom pour tracer l'utilisation de LOGGER.info().
 */
@Rule(
  key = "track-info-method",
  priority = Priority.INFO,
  name = "Utilisation de LOGGER.info.",
  description = "Cette règle vérifie l'utilisation de la méthode LOGGER.info.",
  tags = {"track", "logger", "ma-moulinette"}
)
public class TrackInfoMethodCheck extends AbstractTrackLoggerMethodCheck {

  /**
   * Spécifie le nom de méthode à tracer (info ici).
   *
   * @return le nom de méthode "info".
   */
  @Override
  protected String getMethodName() {
    return "info";
  }

  /**
   * Spécifie le message de logger pour le niveau info.
   *
   * @return le message "Utilisation du Logger en mode : info".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : info";
  }
}
