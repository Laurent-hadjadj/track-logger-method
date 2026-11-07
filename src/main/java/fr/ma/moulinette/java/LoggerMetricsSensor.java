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

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.Metric;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sensor post-analyse qui lit les compteurs accumulés par les checks AST via le LoggerCounter
 * partagé, puis publie les métriques custom déclarées dans LoggerMetrics vers le dashboard
 * SonarQube.
 *
 * Pourquoi pas allIssues() ? SensorContext.allIssues() a été retiré de sonar-plugin-api en 9.x.
 * Les compteurs sont accumulés directement dans les checks lors du parcours AST et transmis ici
 * via le singleton LoggerCounter de portée scanner.
 *
 * Portée des métriques : elles sont sauvegardées au niveau projet via context.project().
 * SonarQube agrège automatiquement vers le parent dans les builds Maven multi-modules.
 *
 * Exécution unique garantie : execute() peut être invoqué plusieurs fois pour une même analyse
 * (scan multi-module, jar de plugin dupliqué dans extensions/plugins/...). Comme l'API de mesure
 * de SonarQube n'accepte qu'une seule sauvegarde par métrique et par composant, la publication
 * est gardée par LoggerCounter.markPublished() : seule la première invocation publie les
 * métriques, les suivantes sont ignorées (avec un log explicite) au lieu d'échouer en silence.
 */
public class LoggerMetricsSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(LoggerMetricsSensor.class);

  private static final Map<LoggerFramework, Metric<Integer>> TOTAL_METRICS = Map.ofEntries(
      Map.entry(LoggerFramework.SLF4J, LoggerMetrics.LOGGER_SLF4J_COUNT),
      Map.entry(LoggerFramework.LOMBOK_SLF4J, LoggerMetrics.LOGGER_LOMBOK_SLF4J_COUNT),
      Map.entry(LoggerFramework.LOG4J2, LoggerMetrics.LOGGER_LOG4J2_COUNT),
      Map.entry(LoggerFramework.LOMBOK_LOG4J2, LoggerMetrics.LOGGER_LOMBOK_LOG4J2_COUNT),
      Map.entry(LoggerFramework.LOG4J1, LoggerMetrics.LOGGER_LOG4J1_COUNT),
      Map.entry(LoggerFramework.JUL, LoggerMetrics.LOGGER_JUL_COUNT),
      Map.entry(LoggerFramework.LOGBACK, LoggerMetrics.LOGGER_LOGBACK_COUNT),
      Map.entry(LoggerFramework.COMMONS_LOGGING, LoggerMetrics.LOGGER_COMMONS_LOGGING_COUNT),
      Map.entry(LoggerFramework.JBOSS, LoggerMetrics.LOGGER_JBOSS_COUNT),
      Map.entry(LoggerFramework.UNKNOWN, LoggerMetrics.LOGGER_UNKNOWN_COUNT));
  private static final Map<LoggerFramework, Metric<Double>> RATIO_METRICS = Map.ofEntries(
      Map.entry(LoggerFramework.SLF4J, LoggerMetrics.LOGGER_SLF4J_RATIO),
      Map.entry(LoggerFramework.LOMBOK_SLF4J, LoggerMetrics.LOGGER_LOMBOK_SLF4J_RATIO),
      Map.entry(LoggerFramework.LOG4J2, LoggerMetrics.LOGGER_LOG4J2_RATIO),
      Map.entry(LoggerFramework.LOMBOK_LOG4J2, LoggerMetrics.LOGGER_LOMBOK_LOG4J2_RATIO),
      Map.entry(LoggerFramework.LOG4J1, LoggerMetrics.LOGGER_LOG4J1_RATIO),
      Map.entry(LoggerFramework.JUL, LoggerMetrics.LOGGER_JUL_RATIO),
      Map.entry(LoggerFramework.LOGBACK, LoggerMetrics.LOGGER_LOGBACK_RATIO),
      Map.entry(LoggerFramework.COMMONS_LOGGING, LoggerMetrics.LOGGER_COMMONS_LOGGING_RATIO),
      Map.entry(LoggerFramework.JBOSS, LoggerMetrics.LOGGER_JBOSS_RATIO),
      Map.entry(LoggerFramework.UNKNOWN, LoggerMetrics.LOGGER_UNKNOWN_RATIO));
  // Ordre des niveaux : error, warn, info, debug, trace. LOGBACK, COMMONS_LOGGING et JBOSS n'ont
  // pas de métrique par niveau.
  private static final Map<LoggerFramework, Map<String, Metric<Integer>>> LEVEL_METRICS =
      Map.ofEntries(
      Map.entry(LoggerFramework.SLF4J, levels(
          LoggerMetrics.LOGGER_SLF4J_ERROR_COUNT, LoggerMetrics.LOGGER_SLF4J_WARN_COUNT,
          LoggerMetrics.LOGGER_SLF4J_INFO_COUNT, LoggerMetrics.LOGGER_SLF4J_DEBUG_COUNT,
          LoggerMetrics.LOGGER_SLF4J_TRACE_COUNT)),
      Map.entry(LoggerFramework.LOMBOK_SLF4J, levels(
          LoggerMetrics.LOGGER_LOMBOK_SLF4J_ERROR_COUNT, LoggerMetrics.LOGGER_LOMBOK_SLF4J_WARN_COUNT,
          LoggerMetrics.LOGGER_LOMBOK_SLF4J_INFO_COUNT, LoggerMetrics.LOGGER_LOMBOK_SLF4J_DEBUG_COUNT,
          LoggerMetrics.LOGGER_LOMBOK_SLF4J_TRACE_COUNT)),
      Map.entry(LoggerFramework.LOG4J2, levels(
          LoggerMetrics.LOGGER_LOG4J2_ERROR_COUNT, LoggerMetrics.LOGGER_LOG4J2_WARN_COUNT,
          LoggerMetrics.LOGGER_LOG4J2_INFO_COUNT, LoggerMetrics.LOGGER_LOG4J2_DEBUG_COUNT,
          LoggerMetrics.LOGGER_LOG4J2_TRACE_COUNT)),
      Map.entry(LoggerFramework.LOMBOK_LOG4J2, levels(
          LoggerMetrics.LOGGER_LOMBOK_LOG4J2_ERROR_COUNT, LoggerMetrics.LOGGER_LOMBOK_LOG4J2_WARN_COUNT,
          LoggerMetrics.LOGGER_LOMBOK_LOG4J2_INFO_COUNT, LoggerMetrics.LOGGER_LOMBOK_LOG4J2_DEBUG_COUNT,
          LoggerMetrics.LOGGER_LOMBOK_LOG4J2_TRACE_COUNT)),
      Map.entry(LoggerFramework.LOG4J1, levels(
          LoggerMetrics.LOGGER_LOG4J1_ERROR_COUNT, LoggerMetrics.LOGGER_LOG4J1_WARN_COUNT,
          LoggerMetrics.LOGGER_LOG4J1_INFO_COUNT, LoggerMetrics.LOGGER_LOG4J1_DEBUG_COUNT,
          LoggerMetrics.LOGGER_LOG4J1_TRACE_COUNT)),
      Map.entry(LoggerFramework.JUL, levels(
          LoggerMetrics.LOGGER_JUL_ERROR_COUNT, LoggerMetrics.LOGGER_JUL_WARN_COUNT,
          LoggerMetrics.LOGGER_JUL_INFO_COUNT, LoggerMetrics.LOGGER_JUL_DEBUG_COUNT,
          LoggerMetrics.LOGGER_JUL_TRACE_COUNT)),
      Map.entry(LoggerFramework.UNKNOWN, levels(
          LoggerMetrics.LOGGER_UNKNOWN_ERROR_COUNT, LoggerMetrics.LOGGER_UNKNOWN_WARN_COUNT,
          LoggerMetrics.LOGGER_UNKNOWN_INFO_COUNT, LoggerMetrics.LOGGER_UNKNOWN_DEBUG_COUNT,
          LoggerMetrics.LOGGER_UNKNOWN_TRACE_COUNT)));

  /** Pourcentage avec une décimale : arrondi de (x * 1000 / total) puis division par 10. */
  private static final double PERCENT_SCALE = 1000.0;
  private static final double PERCENT_DIVISOR = 10.0;

  private final LoggerCounter loggerCounter;

  /**
   * Injection par constructeur — SonarQube fournit le singleton LoggerCounter car il est
   * enregistré comme extension dans MyJavaPlugin.
   *
   * @param loggerCounter le compteur partagé de portée scanner
   */
  public LoggerMetricsSensor(LoggerCounter loggerCounter) {
    this.loggerCounter = loggerCounter;
  }

  // -------------------------------------------------------------------------
  // Sensor contract
  // -------------------------------------------------------------------------

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
        .name("Logger Metrics Sensor")
        .onlyOnLanguage("java");
  }

  @Override
  public void execute(SensorContext context) {
    if (!loggerCounter.markPublished()) {
      LOG.warn("-=LoggerMetricsSensor=- exécution ignorée : les métriques ont déjà été publiées "
          + "pour cette analyse. Si ce message apparaît systématiquement, vérifiez qu'un seul jar "
          + "du plugin track-logger-method est présent dans extensions/plugins/ de votre serveur "
          + "SonarQube (un ancien jar oublié à côté du nouveau provoque ce symptôme).");
      return;
    }

    int grandTotal = loggerCounter.getGrandTotal();
    LOG.info("-=LoggerMetricsSensor=- total appels logger détectés : {}", grandTotal);

    publishTotalPerFramework(context);
    publishPerFrameworkPerLevel(context);
    publishRatios(context, grandTotal);
    publishGrandTotal(context, grandTotal);

    LOG.info("-=LoggerMetricsSensor=- métriques publiées avec succès.");
  }

  // -------------------------------------------------------------------------
  // Publication des métriques
  // -------------------------------------------------------------------------

  private void publishTotalPerFramework(SensorContext context) {
    for (LoggerFramework framework : LoggerFramework.values()) {
      int total = loggerCounter.getTotalForFramework(framework);
      Metric<Integer> metric = totalMetricFor(framework);
      if (metric != null) {
        saveIntMetric(context, metric, total);
      }
    }
  }

  private void publishPerFrameworkPerLevel(SensorContext context) {
    for (LoggerFramework framework : LoggerFramework.values()) {
      for (String level : LoggerCounter.LEVELS) {
        int count = loggerCounter.getCount(framework, level);
        Metric<Integer> metric = levelMetricFor(framework, level);
        if (metric != null) {
          saveIntMetric(context, metric, count);
        }
      }
    }
  }

  private void publishRatios(SensorContext context, int grandTotal) {
    for (LoggerFramework framework : LoggerFramework.values()) {
      int total = loggerCounter.getTotalForFramework(framework);
      double ratio = computeRatio(total, grandTotal);
      Metric<Double> metric = ratioMetricFor(framework);
      if (metric != null) {
        saveDoubleMetric(context, metric, ratio);
      }
    }
  }

  private static double computeRatio(int total, int grandTotal) {
    if (grandTotal == 0) {
      return 0.0;
    }
    double scaled = total * PERCENT_SCALE;
    return Math.round(scaled / grandTotal) / PERCENT_DIVISOR;
  }

  private static void publishGrandTotal(SensorContext context, int grandTotal) {
    saveIntMetric(context, LoggerMetrics.LOGGER_TOTAL_COUNT, grandTotal);
  }

  // -------------------------------------------------------------------------
  // Résolution framework → Metric (tables statiques)
  // -------------------------------------------------------------------------

  private static Map<String, Metric<Integer>> levels(Metric<Integer> error, Metric<Integer> warn,
                                                     Metric<Integer> info, Metric<Integer> debug,
                                                     Metric<Integer> trace) {
    return Map.of(
        LoggerCounter.LEVEL_ERROR, error,
        LoggerCounter.LEVEL_WARN, warn,
        LoggerCounter.LEVEL_INFO, info,
        LoggerCounter.LEVEL_DEBUG, debug,
        LoggerCounter.LEVEL_TRACE, trace);
  }

  private static Metric<Integer> totalMetricFor(LoggerFramework framework) {
    return TOTAL_METRICS.get(framework);
  }

  private static Metric<Integer> levelMetricFor(LoggerFramework framework, String level) {
    Map<String, Metric<Integer>> byLevel = LEVEL_METRICS.get(framework);
    if (byLevel == null) {
      return null;
    }
    return byLevel.get(level);
  }

  private static Metric<Double> ratioMetricFor(LoggerFramework framework) {
    return RATIO_METRICS.get(framework);
  }

  // -------------------------------------------------------------------------
  // Helpers de sauvegarde
  // -------------------------------------------------------------------------

  private static void saveIntMetric(SensorContext context, Metric<Integer> metric, int value) {
    try {
      context.<Integer>newMeasure()
          .forMetric(metric)
          .on(context.project())
          .withValue(value)
          .save();
    } catch (Exception e) {
      LOG.warn("Impossible de sauvegarder la métrique '{}' : {}", metric.key(), e.getMessage());
    }
  }

  private static void saveDoubleMetric(SensorContext context, Metric<Double> metric, double value) {
    try {
      context.<Double>newMeasure()
          .forMetric(metric)
          .on(context.project())
          .withValue(value)
          .save();
    } catch (Exception e) {
      LOG.warn("Impossible de sauvegarder la métrique '{}' : {}", metric.key(), e.getMessage());
    }
  }
}
