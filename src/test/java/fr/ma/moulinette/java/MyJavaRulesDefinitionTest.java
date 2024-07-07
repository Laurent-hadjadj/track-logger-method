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

import org.junit.jupiter.api.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction.Type;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;

import static org.assertj.core.api.Assertions.assertThat;

class MyJavaRulesDefinitionTest {

  @Test
  void test() {
    MyJavaRulesDefinition rulesDefinition = new MyJavaRulesDefinition(new MyJavaRulesPluginTest.MockedSonarRuntime());

    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository(MyJavaRulesDefinition.REPOSITORY_KEY);

    assertThat(repository.name())
    .isNotNull()  // Vérifie que repository.name() n'est pas null
    .isEqualTo(MyJavaRulesDefinition.REPOSITORY_NAME);

    assertThat(repository.language()).isEqualTo("java");
    assertThat(repository.rules()).hasSize(RulesList.getChecks().size());
    assertThat(repository.rules().stream().filter(Rule::template)).isEmpty();

    assertRuleProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }


  private static void assertRuleProperties(Repository repository) {
    Rule rule = repository.rule("track-logger-method");
    assertThat(rule).isNotNull();
    assertThat(rule.name()).isEqualTo("Track Logger method (info, warn, error, debug, trace)");
    assertThat(rule.debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(rule.type()).isEqualTo(RuleType.CODE_SMELL);
  }

  private static void assertAllRuleParametersHaveDescription(Repository repository) {
    for (Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }

}
