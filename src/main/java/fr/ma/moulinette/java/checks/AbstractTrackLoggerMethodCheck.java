/*
 *      Ma-Moulinette
 *  -=track-logger-method=-
 *  -------------------------
 *  Copyright (c) 2015-2026.
 *  Laurent HADJADJ <laurent_h@me.com>.
 *  SPDX-License-Identifier: LGPL-3.0-or-later
 *  Voir le fichier LICENSE à la racine du dépôt.
 */
package fr.ma.moulinette.java.checks;

import fr.ma.moulinette.java.LoggerCounter;

import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de base abstraite pour le suivi des appels aux méthodes de logger dans le code Java.
 *
 * Stratégie de détection, par ordre de priorité :
 *  1. Résolution sémantique du type — utilise receiver.symbolType().fullyQualifiedName() pour
 *     identifier précisément l'interface de logger. La méthode la plus fiable, elle fonctionne
 *     quel que soit le nom de la variable.
 *  2. Annotation Lombok — quand la classe englobante porte une annotation de logging Lombok
 *     (@Slf4j, @Log4j2, ...), le framework détecté est affiné vers la variante Lombok.
 *  3. Repli sur le nom de variable — si la résolution sémantique échoue, les noms conventionnels
 *     (LOGGER, LOG, logger, log) sont utilisés pour tout de même signaler l'appel, classé en
 *     LoggerFramework.UNKNOWN.
 *
 * Intégration avec le compteur : chaque appel détecté incrémente le registre statique
 * LoggerCounter via LoggerCounter.increment(framework, level). Aucune injection d'instance
 * n'est nécessaire car la map de compteurs est un champ statique, accessible depuis les checks
 * que SonarQube instancie sans contrôle du constructeur.
 */
public abstract class AbstractTrackLoggerMethodCheck extends IssuableSubscriptionVisitor {

  private static final Logger LOG =
      LoggerFactory.getLogger(AbstractTrackLoggerMethodCheck.class);

  /** Noms de variable de logger conventionnels utilisés en dernier recours. */
  private static final Set<String> CONVENTIONAL_LOGGER_NAMES =
      Set.of("LOGGER", "LOG", "logger", "log");

  /** Noms simples des annotations Lombok qui génèrent un champ logger. */
  private static final Set<String> LOMBOK_LOGGER_ANNOTATIONS =
      Set.of("Slf4j", "Slf4jX", "Log4j", "Log4j2",
              "CommonsLog", "JBossLog", "Log", "Flogger", "CustomLog");

  // -------------------------------------------------------------------------
  // Contrat abstrait
  // -------------------------------------------------------------------------

  /** @return le nom de la méthode de niveau de log à détecter (ex. "error"). */
  protected abstract String getMethodName();

  /** @return le texte de base de l'issue SonarQube signalée à chaque correspondance. */
  protected abstract String getLoggerMessage();

  // -------------------------------------------------------------------------
  // Contrat IssuableSubscriptionVisitor
  // -------------------------------------------------------------------------

  @Override
  public List<Kind> nodesToVisit() {
    return Collections.singletonList(Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(@Nonnull Tree tree) {
    MethodInvocationTree methodInvocation = (MethodInvocationTree) tree;

    detectFramework(methodInvocation, getMethodName()).ifPresent((LoggerFramework framework) -> {
      // Signale l'issue SonarQube avec le label du framework en suffixe
      reportIssue(methodInvocation, buildIssueMessage(framework));

      // Incrémente le compteur statique — aucune injection d'instance requise
      LoggerCounter.increment(framework, getMethodName());

      LOG.info("-=Logger détecté=- méthode={} framework={}",
          getMethodName(), framework.getLabel());
    });
  }

  // -------------------------------------------------------------------------
  // Détection du framework
  // -------------------------------------------------------------------------

  private static Optional<LoggerFramework> detectFramework(MethodInvocationTree methodInvocation,
                                                           String methodName) {
    ExpressionTree receiver = levelCallReceiver(methodInvocation, methodName);
    if (receiver == null) {
      return Optional.empty();
    }

    // 1. Résolution sémantique du type (la plus fiable)
    LoggerFramework byType = resolveBySemanticType(receiver);
    // 2. Repli sur le nom de variable conventionnel
    boolean byName = byType == LoggerFramework.UNKNOWN
        && receiver.is(Kind.IDENTIFIER)
        && CONVENTIONAL_LOGGER_NAMES.contains(((IdentifierTree) receiver).name());
    if (byType == LoggerFramework.UNKNOWN && !byName) {
      return Optional.empty();
    }

    String fqn = null;
    if (!byName) {
      fqn = byType.getFullyQualifiedName();
    }
    return Optional.of(
        LoggerFramework.fromTypeAndAnnotation(fqn, findLombokAnnotation(methodInvocation)));
  }

  /**
   * Retourne le receveur de l'appel s'il s'agit de {@code <receveur>.<methodName>(...)}, sinon
   * null.
   */
  @Nullable
  private static ExpressionTree levelCallReceiver(MethodInvocationTree methodInvocation,
                                                  String methodName) {
    ExpressionTree expression = methodInvocation.methodSelect();
    if (!expression.is(Kind.MEMBER_SELECT)) {
      return null;
    }
    MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) expression;
    if (methodName.equals(memberSelect.identifier().name())) {
      return memberSelect.expression();
    }
    return null;
  }

  private static LoggerFramework resolveBySemanticType(ExpressionTree receiver) {
    try {
      var type = receiver.symbolType();
      if (type != null && !type.isUnknown()) {
        return LoggerFramework.fromType(type.fullyQualifiedName());
      }
    } catch (Exception e) {
      LOG.debug("Impossible de résoudre le type sémantique du receveur '{}' : {}",
          receiver, e.getMessage());
    }
    return LoggerFramework.UNKNOWN;
  }

  @Nullable
  private static String findLombokAnnotation(Tree node) {
    ClassTree enclosingClass = findEnclosingClass(node);
    if (enclosingClass == null) {
      return null;
    }

    for (AnnotationTree annotation : enclosingClass.modifiers().annotations()) {
      String simpleName = extractSimpleName(annotation.annotationType().toString());
      if (LOMBOK_LOGGER_ANNOTATIONS.contains(simpleName)) {
        return simpleName;
      }
    }
    return null;
  }

  @Nullable
  private static ClassTree findEnclosingClass(Tree node) {
    Tree current = node.parent();
    while (current != null) {
      if (current.is(Kind.CLASS, Kind.ENUM, Kind.RECORD, Kind.INTERFACE)) {
        return (ClassTree) current;
      }
      current = current.parent();
    }
    return null;
  }

  private static String extractSimpleName(String name) {
    int lastDot = name.lastIndexOf('.');
    if (lastDot >= 0) {
      return name.substring(lastDot + 1);
    }
    return name;
  }

  private String buildIssueMessage(LoggerFramework framework) {
    if (framework == LoggerFramework.UNKNOWN) {
      return getLoggerMessage();
    }
    return String.format("%s [%s]", getLoggerMessage(), framework.getLabel());
  }

  protected static Logger getLogger() {
    return LOG;
  }
}
