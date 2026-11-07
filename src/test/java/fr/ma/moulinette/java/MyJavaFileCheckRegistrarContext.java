/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  SPDX-License-Identifier: LGPL-3.0-or-later
 *  Voir le fichier LICENSE à la racine du dépôt.
 */
package fr.ma.moulinette.java;

import org.sonar.api.rule.RuleKey;
import java.util.ArrayList;
import java.util.List;

/**
 * [Description MyJavaFileCheckRegistrarContext]
 */
public class MyJavaFileCheckRegistrarContext {

  public List<RuleKey> mainRuleKeys = new ArrayList<>();
  public List<Class<?>> mainCheckClasses = new ArrayList<>();
  public List<RuleKey> testRuleKeys = new ArrayList<>();
  public List<Class<?>> testCheckClasses = new ArrayList<>();

  public void registerMainRule(RuleKey ruleKey, Class<?> checkClass) {
    mainRuleKeys.add(ruleKey);
    mainCheckClasses.add(checkClass);
  }

  public void registerTestRule(RuleKey ruleKey, Class<?> checkClass) {
    testRuleKeys.add(ruleKey);
    testCheckClasses.add(checkClass);
  }
}
