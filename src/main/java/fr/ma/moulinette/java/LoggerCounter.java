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

import fr.ma.moulinette.java.checks.LoggerFramework;
import org.sonar.api.scanner.ScannerSide;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Compteur partagé et thread-safe qui accumule les statistiques d'appels logger pendant
 * l'analyse AST.
 *
 * L'API CheckRegistrar de SonarQube 9.9 n'accepte que des classes de check, pas des instances :
 * l'injection par constructeur dans les checks est donc impossible. Cette classe expose à la
 * place un registre statique, réinitialisé par LoggerMetricsSensor en début d'exécution,
 * incrémenté par AbstractTrackLoggerMethodCheck via la méthode statique increment(), et lu par
 * LoggerMetricsSensor pour publier les métriques.
 *
 * L'annotation @ScannerSide conserve l'instance dans le conteneur IoC de SonarQube afin qu'elle
 * puisse être injectée par constructeur dans LoggerMetricsSensor.
 *
 * Les compteurs AtomicInteger et la ConcurrentHashMap rendent toutes les opérations sûres même
 * pendant l'analyse parallèle des fichiers par SonarQube.
 */
@ScannerSide
public class LoggerCounter {

  // Niveaux de log suivis — constantes partagées avec LoggerMetrics et LoggerMetricsSensor
  // pour éviter de dupliquer ces littéraux dans chacune de ces classes.
  protected static final String LEVEL_ERROR = "error";
  protected static final String LEVEL_WARN = "warn";
  protected static final String LEVEL_INFO = "info";
  protected static final String LEVEL_DEBUG = "debug";
  protected static final String LEVEL_TRACE = "trace";

  /** Tous les niveaux de log suivis. */
  protected static final List<String> LEVELS =
      List.of(LEVEL_ERROR, LEVEL_WARN, LEVEL_INFO, LEVEL_DEBUG, LEVEL_TRACE);

  // Registre statique — accessible par les checks sans injection de dépendance
  private static final Map<LoggerFramework, Map<String, AtomicInteger>> STATIC_COUNTS =
      buildCounterMap();

  // Verrou garantissant que LoggerMetricsSensor.execute() ne sauvegarde les métriques qu'une
  // seule fois par analyse — l'API de mesure de SonarQube n'accepte qu'une seule sauvegarde par
  // métrique et par composant, et le sensor peut être invoqué plusieurs fois (scan multi-module,
  // jar de plugin dupliqué dans extensions/plugins/...).
  private static final AtomicBoolean PUBLISHED = new AtomicBoolean(false);

  // -------------------------------------------------------------------------
  // API statique — utilisée par AbstractTrackLoggerMethodCheck
  // -------------------------------------------------------------------------

  /**
   * Incrémente le compteur pour le framework et le niveau donnés.
   * Appelée une fois par appel logger détecté par les checks AST.
   *
   * @param framework le framework de logging détecté
   * @param level     le nom de la méthode de niveau de log (error / warn / info / debug / trace)
   */
  public static void increment(LoggerFramework framework, String level) {
    Map<String, AtomicInteger> levelMap = STATIC_COUNTS.get(framework);
    if (levelMap == null) {
      return;
    }
    AtomicInteger counter = levelMap.get(level);
    if (counter == null) {
      return;
    }
    counter.incrementAndGet();
  }

  /**
   * Remet à zéro tous les compteurs ainsi que le verrou markPublished().
   *
   * Non appelée automatiquement : chaque invocation de mvn sonar:sonar / du scanner tourne dans
   * sa propre JVM, donc l'état statique démarre déjà propre. Exposée pour les tests, et pour tout
   * scénario de scan embarqué avec réutilisation de JVM qui enchaînerait plusieurs analyses
   * indépendantes.
   */
  public static void reset() {
    STATIC_COUNTS.values().forEach(levelMap ->
        levelMap.values().forEach(c -> c.set(0)));
    PUBLISHED.set(false);
  }

  /**
   * Marque de façon atomique les métriques de cette analyse comme publiées.
   *
   * @return true la première fois que la méthode est appelée depuis le dernier reset() —
   *         l'appelant doit alors publier les métriques. false pour tout appel suivant : les
   *         métriques ont déjà été publiées et ne doivent pas être sauvegardées à nouveau,
   *         puisque l'API de mesure de SonarQube n'accepte qu'une seule sauvegarde par métrique
   *         et par composant, par analyse.
   */
  public boolean markPublished() {
    return PUBLISHED.compareAndSet(false, true);
  }

  // -------------------------------------------------------------------------
  // API d'instance — utilisée par LoggerMetricsSensor (injectée par l'IoC SonarQube)
  // -------------------------------------------------------------------------

  /**
   * Retourne le compteur pour un framework et un niveau donnés.
   *
   * @param framework le framework de logging
   * @param level     le nom de la méthode de niveau de log
   * @return le nombre d'appels détectés, ou 0 si non suivi
   */
  public int getCount(LoggerFramework framework, String level) {
    var levelMap = STATIC_COUNTS.get(framework);
    if (levelMap == null) {
      return 0;
    }
    AtomicInteger counter = levelMap.get(level);
    if (counter == null) {
      return 0;
    }
    return counter.get();
  }

  /**
   * Retourne le total tous niveaux confondus pour un framework donné.
   *
   * @param framework le framework de logging
   * @return la somme des compteurs de tous les niveaux pour ce framework
   */
  public int getTotalForFramework(LoggerFramework framework) {
    Map<String, AtomicInteger> levelMap = STATIC_COUNTS.get(framework);
    if (levelMap == null) {
      return 0;
    }
    return levelMap.values().stream().mapToInt(AtomicInteger::get).sum();
  }

  /**
   * Retourne le grand total tous frameworks et tous niveaux confondus.
   *
   * @return le nombre total d'appels logger détectés
   */
  public int getGrandTotal() {
    var total = 0;
    for (LoggerFramework framework : LoggerFramework.values()) {
      total += getTotalForFramework(framework);
    }
    return total;
  }

  /**
   * Retourne un instantané de tous les compteurs sous forme d'entiers simples (pour le
   * logging/débogage).
   *
   * @return une map framework vers (niveau vers compteur)
   */
  public Map<LoggerFramework, Map<String, Integer>> snapshot() {
    return STATIC_COUNTS.entrySet().stream().collect(Collectors.toUnmodifiableMap(
        Map.Entry::getKey,
        entry -> entry.getValue().entrySet().stream().collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey, levelEntry -> levelEntry.getValue().get()))));
  }

  // -------------------------------------------------------------------------
  // Aides internes
  // -------------------------------------------------------------------------

  private static Map<LoggerFramework, Map<String, AtomicInteger>> buildCounterMap() {
    return Arrays.stream(LoggerFramework.values())
        .map(framework -> Map.entry(framework, newLevelMap()))
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Map<String, AtomicInteger> newLevelMap() {
    Map<String, AtomicInteger> levelMap = new ConcurrentHashMap<>();
    LEVELS.forEach(level -> levelMap.put(level, new AtomicInteger(0)));
    return levelMap;
  }
}
