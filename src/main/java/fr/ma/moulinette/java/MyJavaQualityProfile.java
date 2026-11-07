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

import javax.annotation.Nonnull;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.plugins.java.Java;
/**
 * Définit un profil qualité custom pour le plugin SonarQube.
 * Un profil qualité est un ensemble de règles activées pour un langage donné.
 */
public final class MyJavaQualityProfile implements BuiltInQualityProfilesDefinition {

  // Nom du profil qualité custom
  public static final String QUALITY_PROFILE_NAME = "Ma-Moulinette";

  /**
   * Définit le profil qualité custom en activant les règles concernées.
   *
   * @param context le contexte dans lequel le profil est défini.
   */
  @Override
  public void define(@Nonnull Context context) {

    // Crée un nouveau profil qualité intégré pour le langage Java
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(QUALITY_PROFILE_NAME, Java.KEY);

    // Active chaque règle définie dans RulesList pour ce profil qualité
    RulesList.getCheckKeys().forEach(key -> profile.activateRule(MyJavaRulesDefinition.REPOSITORY_KEY, key));

    profile.done();
  }
}
