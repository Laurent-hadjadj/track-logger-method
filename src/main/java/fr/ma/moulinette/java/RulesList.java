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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.check.Rule;
import fr.ma.moulinette.java.checks.TrackInfoMethodCheck;
import fr.ma.moulinette.java.checks.TrackWarnMethodCheck;
import fr.ma.moulinette.java.checks.TrackErrorMethodCheck;
import fr.ma.moulinette.java.checks.TrackDebugMethodCheck;
import fr.ma.moulinette.java.checks.TrackTraceMethodCheck;

/**
 * Fournit les listes de checks Java (règles) utilisées par le plugin SonarQube.
 * Source unique de vérité pour récupérer les checks et leurs clés en vue de l'activation
 * des règles.
 */
public final class RulesList {

  // Constructeur privé pour empêcher l'instanciation
  private RulesList() {}

  /**
   * Récupère tous les checks Java (principaux et de test).
   *
   * @return une liste non modifiable de tous les checks Java.
   */
  public static List<Class<? extends JavaCheck>> getChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    checks.addAll(getJavaChecks());
    checks.addAll(getJavaTestChecks());
    return Collections.unmodifiableList(checks);
  }

  /**
   * Récupère la liste des checks Java principaux.
   *
   * @return une liste des checks Java principaux.
   */
  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    // Ajoute chaque classe de check à la liste
    checks.add(TrackInfoMethodCheck.class);
    checks.add(TrackErrorMethodCheck.class);
    checks.add(TrackWarnMethodCheck.class);
    checks.add(TrackDebugMethodCheck.class);
    checks.add(TrackTraceMethodCheck.class);
    return checks;
  }

  /**
   * Récupère la liste des checks Java de test.
   *
   * @return une liste vide de checks Java de test (aucun défini).
   */
  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    // Aucun check de test défini
    return Collections.emptyList();
  }

  /**
   * Récupère les clés de tous les checks.
   * Utilisé pour activer les règles dans le profil qualité SonarQube.
   *
   * @return une liste non modifiable de clés de règles.
   */
  public static List<String> getCheckKeys() {
    // Extrait la clé depuis l'annotation @Rule
    return getChecks().stream()
            .map(clazz -> clazz.getAnnotation(Rule.class).key())
            .toList();
  }
}
