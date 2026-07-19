/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
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

  private static final String RULE_INFO = "track-info-method";
  private static final String RULE_ERROR = "track-error-method";
  private static final String RULE_WARN = "track-warn-method";
  private static final String RULE_DEBUG = "track-debug-method";
  private static final String RULE_TRACE = "track-trace-method";

  @Test
  /** On test que le repository name n'est pass null avant de tester l'égalité */
  @SuppressWarnings("findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
  void test() {
    MyJavaRulesDefinition rulesDefinition = new MyJavaRulesDefinition(new MyJavaPluginTest.MockedSonarRuntime());

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
    assertThat(repository.rule(RULE_INFO))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method INFO");

    assertThat(repository.rule(RULE_ERROR))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method ERROR");

    assertThat(repository.rule(RULE_WARN))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method WARN");

    assertThat(repository.rule(RULE_DEBUG))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method DEBUG");

    assertThat(repository.rule(RULE_TRACE))
      .isNotNull()
      .extracting(Rule::name)
      .isEqualTo("Track Logger method TRACE");

    assertThat(repository.rule(RULE_INFO).debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule(RULE_ERROR).debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule(RULE_WARN).debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule(RULE_DEBUG).debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(repository.rule(RULE_TRACE).debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);

    assertThat(repository.rule(RULE_INFO).type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule(RULE_ERROR).type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule(RULE_WARN).type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule(RULE_DEBUG).type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule(RULE_TRACE).type()).isEqualTo(RuleType.CODE_SMELL);
  }

  private static void assertAllRuleParametersHaveDescription(Repository repository) {
    for (Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }
}
