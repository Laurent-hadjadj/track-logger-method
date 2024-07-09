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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [Description AbstractTrackLoggerMethodCheck]
 */
public abstract class AbstractTrackLoggerMethodCheck extends IssuableSubscriptionVisitor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTrackLoggerMethodCheck.class);

  protected static Logger getLogger() {
    return LOGGER;
  }

  protected abstract String getMethodName();
  protected abstract String getLoggerMessage();

  @Override
  public List<Kind> nodesToVisit() {
    return Collections.singletonList(Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree methodInvocation = (MethodInvocationTree) tree;
    if (isLoggerMethod(methodInvocation, getMethodName())) {
      reportIssue(methodInvocation, getLoggerMessage());
      LOGGER.info("-=J'ai trouvé un appel au logger=- ({}).", methodInvocation);
    }
  }

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
