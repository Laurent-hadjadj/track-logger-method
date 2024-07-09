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
package fr.ma.moulinette.java.checks;

import org.sonar.check.Rule;
import org.sonar.check.Priority;

/**
 * Custom SonarQube rule implementation to track usage of LOGGER.info().
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
   * Specifies the method name to track (info in this case).
   *
   * @return The method name "info".
   */
  @Override
  protected String getMethodName() {
    return "info";
  }

  /**
   * Specifies the logger message template for info level.
   *
   * @return The logger message "Utilisation du Logger en mode : info".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : info";
  }
}
