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

/**
 * [Description NoLoggerUsage]
 */
public class NoLoggerUsage {

    public void doSomething() {
        // Aucun appel de méthode de journalisation ici
        System.out.println("Doing something...");
    }

}
