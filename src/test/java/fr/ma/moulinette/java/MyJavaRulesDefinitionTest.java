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

/**
 * [Description MyJavaRulesDefinitionTest]
 */
class MyJavaRulesDefinitionTest {

  @Test
  /** On test que le repository name n'est pass null avant de tester l'égalité */
  @SuppressWarnings("findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
  void test() {
    MyJavaRulesDefinition rulesDefinition = new MyJavaRulesDefinition(new MyJavaRulesPluginTest.MockedSonarRuntime());

    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    Repository repository = context.repository(MyJavaRulesDefinition.REPOSITORY_KEY);

    assertThat(repository.name())
      .isNotNull()
      .isEqualTo(MyJavaRulesDefinition.REPOSITORY_NAME);

    assertThat(repository.language()).isEqualTo("java");
    assertThat(repository.rules()).hasSize(RulesList.getChecks().size());
    assertThat(repository.rules().stream().filter(Rule::template)).isEmpty();

    assertRuleProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }

  @SuppressWarnings("findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
  private static void assertRuleProperties(Repository repository) {
    assertThat(repository.rule("track-info-method"))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method INFO");

    assertThat(repository.rule("track-error-method"))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method ERROR");

    assertThat(repository.rule("track-warn-method"))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method WARN");

    assertThat(repository.rule("track-debug-method"))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method DEBUG");

    assertThat(repository.rule("track-info-method").debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule("track-error-method").debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule("track-warn-method").debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule("track-debug-method").debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);

    assertThat(repository.rule("track-info-method").type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule("track-error-method").type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule("track-warn-method").type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule("track-debug-method").type()).isEqualTo(RuleType.CODE_SMELL);
  }

  private static void assertAllRuleParametersHaveDescription(Repository repository) {
    for (Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }
}
