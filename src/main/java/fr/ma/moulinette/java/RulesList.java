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
import org.sonar.plugins.java.api.JavaCheck;
import fr.ma.moulinette.java.checks.TrackInfoMethodCheck;
import fr.ma.moulinette.java.checks.TrackWarnMethodCheck;
import fr.ma.moulinette.java.checks.TrackErrorMethodCheck;
import fr.ma.moulinette.java.checks.TrackDebugMethodCheck;

/**
 * [Description RulesList]
 */
public final class RulesList {

  private RulesList() {
  }

  public static List<Class<? extends JavaCheck>> getChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    checks.addAll(getJavaChecks());
    checks.addAll(getJavaTestChecks());
    return Collections.unmodifiableList(checks);
  }

  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    checks.add(TrackInfoMethodCheck.class);
    checks.add(TrackErrorMethodCheck.class);
    checks.add(TrackWarnMethodCheck.class);
    checks.add(TrackDebugMethodCheck.class);
    return checks;
  }

  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    return Collections.emptyList();
  }

}
