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

/**
 * [Description NoLoggerUsage]
 */
public class NoLoggerUsage {

    public void doSomething() {
        // Aucun appel de méthode de journalisation ici
        System.out.println("Doing something...");
    }

}
