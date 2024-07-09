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

import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for tracking logger method invocations in Java code.
 */
public abstract class AbstractTrackLoggerMethodCheck extends IssuableSubscriptionVisitor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTrackLoggerMethodCheck.class);

  /**
   * Returns the logger instance used by subclasses.
   *
   * @return The logger instance.
   */
  protected static Logger getLogger() {
    return LOGGER;
  }

  /**
   * Subclasses must implement this method to specify the logger method name to track.
   *
   * @return The name of the logger method.
   */
  protected abstract String getMethodName();

  /**
   * Subclasses must implement this method to specify the logger message template.
   *
   * @return The logger message template.
   */
  protected abstract String getLoggerMessage();

  /**
   * Specifies the kind of syntax nodes to visit during analysis.
   *
   * @return A list containing only the METHOD_INVOCATION syntax kind.
   */
  @Override
  public List<Kind> nodesToVisit() {
    return Collections.singletonList(Kind.METHOD_INVOCATION);
  }

  /**
   * Visits each METHOD_INVOCATION node in the syntax tree and checks if it matches the logger method to track.
   *
   * @param tree The METHOD_INVOCATION syntax tree node.
   */
  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocation = (MethodInvocationTree) tree;
    if (isLoggerMethod(methodInvocation, getMethodName())) {
      reportIssue(methodInvocation, getLoggerMessage());
      LOGGER.info("-=J'ai trouvé un appel au logger=- ({}).", methodInvocation);
    }
  }

  /**
   * Checks if the given method invocation matches the logger method to track.
   *
   * @param methodInvocation The method invocation syntax tree node.
   * @param methodName       The name of the logger method to track.
   * @return true if the method invocation matches the logger method, false otherwise.
   */
  private static boolean isLoggerMethod(MethodInvocationTree methodInvocation, String methodName) {
    ExpressionTree expression = methodInvocation.methodSelect();
    if (!expression.is(Kind.MEMBER_SELECT)) {
      return false;
    }
    MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) expression;
    var identifier = memberSelect.identifier().name();
    var expressionName = memberSelect.expression().toString();
    return ("LOGGER".equals(expressionName) || "log".equals(expressionName)) && methodName.equals(identifier);
  }
}
