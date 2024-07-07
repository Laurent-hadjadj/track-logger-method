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
package fr.ma.moulinette.java.checks;

import org.sonar.check.Rule;
import org.sonar.check.Priority;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Rule(
  key = "track-logger-method",
  priority = Priority.MAJOR,
  name = "Utilisation d'un Logger.",
  description = "Cette règle vérifie l'utilisation des méthodes du LOGGER telles que info, warn, error, debug.",
  tags = {"track", "logger", "ma-moulinette"}
)
public class TrackLoggerMethodCheck extends IssuableSubscriptionVisitor {

  // On active les logs
  private static final Logger LOGGER = LoggerFactory.getLogger(TrackLoggerMethodCheck.class);

  // Message affiché
  private static final String MESSAGE = "Utilisation de la méthode : ";

  // Liste des méthode que l'on recherche
  private static final Set<String> LOGGER_METHODS = new LinkedHashSet<>(Arrays.asList("info", "warn", "error", "debug"));

  @Override
  public List<Kind> nodesToVisit() {
    return Collections.singletonList(Kind.METHOD_INVOCATION);
  }

  /** On cherche si une méthode de type logger (info, error, warn..) est invoquée. */
  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocation = (MethodInvocationTree) tree;
    if (isLoggerMethod(methodInvocation)) {
      // Récupérer le nom de la méthode appelée
      MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) methodInvocation.methodSelect();
      String methodName = memberSelect.identifier().name();
      // Construire le message avec le type de méthode
      String message = MESSAGE + methodName;
      reportIssue(methodInvocation, message);
      LOGGER.info("Utilisation de la méthode : {}", methodName);
    }
  }

  /**
   * @param methodInvocation
   * @return
   */
  public static boolean isLoggerMethod(MethodInvocationTree methodInvocation) {
    ExpressionTree expression = methodInvocation.methodSelect();

    if (!expression.is(Kind.MEMBER_SELECT)) {
      return false;
    }

    MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) expression;
    var identifier = memberSelect.identifier().name();
    var expressionName = memberSelect.expression().toString();

    // Vérifier si la méthode est appelée sur LOGGER ou sur log et si c'est une des méthodes de journalisation.
    return ("LOGGER".equals(expressionName) || "log".equals(expressionName))
      && LOGGER_METHODS.contains(identifier);
  }

}
