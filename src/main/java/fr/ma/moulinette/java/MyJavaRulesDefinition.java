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
 * Declare rule metadata in server repository of rules.
 * That allows to list the rules in the page "Rules".
 */
public class MyJavaRulesDefinition implements RulesDefinition {

  // don't change that because the path is hard coded in CheckVerifier
  private static final String RESOURCE_BASE_PATH = "org/sonar/l10n/java/rules/java";

  public static final String REPOSITORY_KEY = "track-logger-method";
  public static final String REPOSITORY_NAME = "java";

  // Add the rule keys of the rules which need to be considered as template-rules
  private static final Set<String> RULE_TEMPLATES_KEY = Collections.emptySet();

  private final SonarRuntime runtime;

  public MyJavaRulesDefinition(SonarRuntime runtime) {
    this.runtime = runtime;
  }

  @Override
  public void define(Context context) {

    var repository = context.createRepository(REPOSITORY_KEY, REPOSITORY_NAME).setName(REPOSITORY_NAME);
    var ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, runtime);

    ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(RulesList.getChecks()));

    setTemplates(repository);

    // On enregistre.
    repository.done();
  }

  private static void setTemplates(NewRepository repository) {
    RULE_TEMPLATES_KEY.stream()
      .map(repository::rule)
      .filter(Objects::nonNull)
      .forEach(rule -> rule.setTemplate(true));
  }
}
