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

/**
 * Package fr.ma.moulinette.java.checks is the root package for MaMoulinette's Java code.
 * It contains all the Java classes and resources related to MaMoulinette's functionality.
 */
package fr.ma.moulinette.java.checks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class TrackLoggerMethodCheckTest {

    private TrackLoggerMethodCheck check;

    @BeforeEach
    public void setUp() {
        check = new TrackLoggerMethodCheck();
    }

    @Test
    void testLoggerMethods() {
        CheckVerifier.newVerifier()
                .onFile("src/test/files/TrackLoggerMethodCheck.java")
                .withCheck(check)
                .verifyIssues();
    }

    @Test
    void testNoLoggerMethods() {
        CheckVerifier.newVerifier()
                .onFile("src/test/files/NoLoggerUsage.java")
                .withCheck(check)
                .verifyNoIssues();
    }
}
