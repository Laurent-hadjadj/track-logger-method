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

import org.sonar.api.Plugin;

/**
 * Entry point of your plugin containing your custom rules.
 * This class implements the SonarQube Plugin interface.
 */
public class MyJavaPlugin implements Plugin {

  /**
   * Method to define extensions (components) provided by this plugin.
   *
   * @param context The context in which extensions are defined.
   */
  @Override
  public void define(Context context) {
    // Register the MyJavaRulesDefinition as a server-side extension,
    // which defines the rules repository and rule metadata.
    context.addExtension(MyJavaRulesDefinition.class);

    // Register the MyJavaFileCheckRegistrar as a batch extension,
    // which registers custom checks (rules) for Java files.
    context.addExtension(MyJavaFileCheckRegistrar.class);

    // Register the MyJavaQualityProfile as an extension to provide custom quality profile
    // to SonarQube, ensuring it is available for use.
    context.addExtension(MyJavaQualityProfile.class);
  }
}
