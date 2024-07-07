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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggerClass {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

  public void info() {
    LOGGER.info("Catch logger INFO"); // Noncompliant {{Utilisation de la méthode : info}}
  }

  private debug() {
    LOGGER.debug("Catch logger DEBUG"); // Noncompliant {{Utilisation de la méthode : debug}}
    return null;
  }

  public void error() {
    LOGGER.error("Catch logger ERROR"); // Noncompliant {{Utilisation de la méthode : error}}
  }

  public void warn() {
    LOGGER.warn("Catch logger WARN");  // Noncompliant {{Utilisation de la méthode : warn}}
  }

  public void trace() {
    LOGGER.trace("No Catch logger TRACE"); // Compliant
  }
}
