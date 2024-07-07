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
import fr.ma.moulinette.java.checks.TrackLoggerMethodCheck;

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
    return Collections.singletonList(TrackLoggerMethodCheck.class);
  }


  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    return Collections.emptyList();
  }

}
