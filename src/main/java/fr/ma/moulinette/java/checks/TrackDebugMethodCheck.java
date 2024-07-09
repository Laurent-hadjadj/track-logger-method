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
 * Custom SonarQube rule implementation to track usage of LOGGER.debug().
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
   * Specifies the method name to track (debug in this case).
   *
   * @return The method name "debug".
   */
  @Override
  protected String getMethodName() {
    return "debug";
  }

  /**
   * Specifies the logger message template for debug level.
   *
   * @return The logger message "Utilisation du Logger en mode : debug".
   */
  @Override
  protected String getLoggerMessage() {
    return "Utilisation du Logger en mode : debug";
  }
}
