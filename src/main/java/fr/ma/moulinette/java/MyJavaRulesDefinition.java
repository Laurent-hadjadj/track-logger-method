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
import java.util.Objects;
import java.util.Set;
import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

/**
 * This class declares rule metadata in the server repository of rules for the Java plugin.
 * It allows the rules to be listed on the "Rules" page in SonarQube.
 */
public class MyJavaRulesDefinition implements RulesDefinition {

  // Path to the rule metadata files
  private static final String RESOURCE_BASE_PATH = "org/sonar/l10n/java/rules/java";

  // Repository key and name
  public static final String REPOSITORY_KEY = "track-logger-method";
  public static final String REPOSITORY_NAME = "java";

  // Set of rule keys that should be considered as template rules
  private static final Set<String> RULE_TEMPLATES_KEY = Collections.emptySet();

  // SonarQube runtime environment
  private final SonarRuntime runtime;

  /**
   * Constructor that initializes the SonarQube runtime environment.
   *
   * @param runtime The SonarQube runtime instance.
   */
  public MyJavaRulesDefinition(SonarRuntime runtime) {
    this.runtime = runtime;
  }

  /**
   * Method to define the rules repository and load rule metadata.
   *
   * @param context The context in which the rules repository is defined.
   */
  @Override
  public void define(Context context) {
    // Create a new repository for the rules with the specified key and name
    var repository = context.createRepository(REPOSITORY_KEY, REPOSITORY_NAME).setName(REPOSITORY_NAME);

    // Load rule metadata from annotated classes using RuleMetadataLoader
    var ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, runtime);
    ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(RulesList.getChecks()));

    // Set any rules marked as templates in the repository
    setTemplates(repository);

    // Mark the repository as done, completing its definition
    repository.done();
  }

  /**
   * Method to set certain rules in the repository as templates.
   *
   * @param repository The repository where rules are defined.
   */
  private static void setTemplates(NewRepository repository) {
    RULE_TEMPLATES_KEY.stream()
      .map(repository::rule)
      .filter(Objects::nonNull)
      .forEach(rule -> rule.setTemplate(true));
  }
}

