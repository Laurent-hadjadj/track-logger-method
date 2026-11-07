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

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

/**
 * [Description TrackLoggerMethodCheckTest]
 */
class TrackLoggerMethodCheckTest {

    private static final String NO_ASSERT = "pmd-unit-tests:JUnitTestsShouldIncludeAssert";

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testInfoMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackInfoMethodCheck.java")
            .withCheck(new TrackInfoMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testInfoMethodLombokSlf4j() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackInfoLombokSlf4jCheck.java")
            .withCheck(new TrackInfoMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testErrorMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackErrorMethodCheck.java")
            .withCheck(new TrackErrorMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testWarnMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackWarnMethodCheck.java")
            .withCheck(new TrackWarnMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testDebugMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackDebugMethodCheck.java")
            .withCheck(new TrackDebugMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testTraceMethod() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/TrackTraceMethodCheck.java")
            .withCheck(new TrackTraceMethodCheck())
            .verifyIssues();
    }

    @Test
    @SuppressWarnings(NO_ASSERT)
    void testNoLoggerMethods() {
        CheckVerifier.newVerifier()
            .onFile("src/test/files/NoLoggerUsage.java")
            .withCheck(new TrackInfoMethodCheck())
            .verifyNoIssues();
    }
}
