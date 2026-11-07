/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  SPDX-License-Identifier: LGPL-3.0-or-later
 *  Voir le fichier LICENSE à la racine du dépôt.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggerClass {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

  public void trace() {
    LOGGER.trace("Catch logger TRACE"); // Noncompliant {{Utilisation du Logger en mode : trace}}
  }

}
