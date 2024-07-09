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

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.plugins.java.Java;
/**
 * This class defines a custom quality profile for the SonarQube plugin.
 * A quality profile is a set of rules that are activated for a specific language.
 */
public final class MyJavaQualityProfile implements BuiltInQualityProfilesDefinition {

  // Name of the custom quality profile
  static final String QUALITY_PROFILE_NAME = "Ma-Moulinette";

  /**
   * Defines the custom quality profile by activating specific rules.
   *
   * @param context The context in which the profile is defined.
   */
  @Override
  public void define(Context context) {

    // Create a new built-in quality profile for Java language
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(QUALITY_PROFILE_NAME, Java.KEY);

    // Activate each rule defined in the RulesList for this quality profile
    RulesList.getCheckKeys().forEach(key -> profile.activateRule(MyJavaRulesDefinition.REPOSITORY_KEY, key));

    profile.done();
  }
}
