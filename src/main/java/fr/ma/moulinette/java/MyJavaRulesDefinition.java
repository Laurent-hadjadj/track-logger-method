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
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

/**
 * Déclare les métadonnées de règles dans le repository serveur du plugin Java.
 * Permet aux règles d'apparaître sur la page "Règles" de SonarQube.
 */
public class MyJavaRulesDefinition implements RulesDefinition {

  // Chemin vers les fichiers de métadonnées de règles
  private static final String RESOURCE_BASE_PATH = "org/sonar/l10n/java/rules/java";

  // Clé et nom du repository
  public static final String REPOSITORY_KEY = "track-logger-method";
  public static final String REPOSITORY_NAME = "java";

  // Ensemble des clés de règles à considérer comme règles de template
  private static final Set<String> RULE_TEMPLATES_KEY = Collections.emptySet();

  // Environnement d'exécution SonarQube
  private final SonarRuntime runtime;

  /**
   * Constructeur qui initialise l'environnement d'exécution SonarQube.
   *
   * @param runtime l'instance du runtime SonarQube.
   */
  public MyJavaRulesDefinition(SonarRuntime runtime) {
    this.runtime = runtime;
  }

  /**
   * Définit le repository de règles et charge les métadonnées des règles.
   *
   * @param context le contexte dans lequel le repository de règles est défini.
   */
  @Override
  public void define(@Nonnull Context context) {
    // Crée un nouveau repository pour les règles avec la clé et le nom donnés
    var repository = context.createRepository(REPOSITORY_KEY, REPOSITORY_NAME).setName(REPOSITORY_NAME);

    // Charge les métadonnées de règles depuis les classes annotées via RuleMetadataLoader
    var ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, runtime);
    ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(RulesList.getChecks()));

    // Marque comme templates les règles concernées dans le repository
    setTemplates(repository);

    // Termine la définition du repository
    repository.done();
  }

  /**
   * Marque certaines règles du repository comme templates.
   *
   * @param repository le repository dans lequel les règles sont définies.
   */
  private static void setTemplates(NewRepository repository) {
    RULE_TEMPLATES_KEY.stream()
      .map(repository::rule)
      .filter(Objects::nonNull)
      .forEach(rule -> rule.setTemplate(true));
  }
}
