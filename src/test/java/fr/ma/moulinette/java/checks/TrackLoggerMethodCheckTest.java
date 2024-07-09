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

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

/**
 * [Description TrackLoggerMethodCheckTest]
 */
class TrackLoggerMethodCheckTest {

    @Test
    @SuppressWarnings("pmd-unit-tests:JUnitTestsShouldIncludeAssert")
    void testInfoMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackInfoMethodCheck.java")
            .withCheck(new TrackInfoMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings("pmd-unit-tests:JUnitTestsShouldIncludeAssert")
    void testErrorMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackErrorMethodCheck.java")
            .withCheck(new TrackErrorMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings("pmd-unit-tests:JUnitTestsShouldIncludeAssert")
    void testWarnMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackWarnMethodCheck.java")
            .withCheck(new TrackWarnMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings("pmd-unit-tests:JUnitTestsShouldIncludeAssert")
    void testDebugMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackDebugMethodCheck.java")
            .withCheck(new TrackDebugMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings("pmd-unit-tests:JUnitTestsShouldIncludeAssert")
    void testNoLoggerMethods() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/NoLoggerUsage.java")
            .withCheck(new TrackInfoMethodCheck())
            .verifyNoIssues();
    }
}
