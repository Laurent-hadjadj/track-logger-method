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
package fr.ma.moulinette.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.check.Rule;
import fr.ma.moulinette.java.checks.TrackInfoMethodCheck;
import fr.ma.moulinette.java.checks.TrackWarnMethodCheck;
import fr.ma.moulinette.java.checks.TrackErrorMethodCheck;
import fr.ma.moulinette.java.checks.TrackDebugMethodCheck;

/**
 * This class provides lists of Java checks (rules) to be used in the SonarQube plugin.
 * It includes methods to retrieve the checks and their keys for rule activation.
 */
public final class RulesList {

  // Private constructor to prevent instantiation
  private RulesList() {}

  /**
   * Retrieves all Java checks (both main and test).
   *
   * @return An unmodifiable list of all Java checks.
   */
  public static List<Class<? extends JavaCheck>> getChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    checks.addAll(getJavaChecks());
    checks.addAll(getJavaTestChecks());
    return Collections.unmodifiableList(checks);
  }

  /**
   * Retrieves the list of main Java checks.
   *
   * @return A list of main Java checks.
   */
  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    // Add each check class to the list
    checks.add(TrackInfoMethodCheck.class);
    checks.add(TrackErrorMethodCheck.class);
    checks.add(TrackWarnMethodCheck.class);
    checks.add(TrackDebugMethodCheck.class);
    return checks;
  }

  /**
   * Retrieves the list of Java test checks.
   *
   * @return An empty list of Java test checks (no test checks defined).
   */
  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    // No test checks are defined
    return Collections.emptyList();
  }

  /**
   * Retrieves the keys of all checks.
   * This is used to activate the rules in the SonarQube quality profile.
   *
   * @return A list of rule keys.
   */
  public static List<String> getCheckKeys() {
    // Extract the key from the @Rule annotation
    return getChecks().stream()
            .map(clazz -> clazz.getAnnotation(Rule.class).key())
            .collect(Collectors.toList());
  }
}
