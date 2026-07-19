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

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * Déclare toutes les métriques custom exposées par le plugin track-logger-method.
 *
 * Convention de nommage des métriques :
 *   logger_(framework)_count          — total d'appels pour un framework
 *   logger_(framework)_(niveau)_count — appels par framework et par niveau
 *   logger_(framework)_ratio          — % du total des appels logger (0.0 à 100.0)
 *
 * Toutes les métriques sont de type INTEGER ou FLOAT, portées au niveau FICHIER par SonarQube
 * mais agrégées automatiquement au niveau module puis projet par la plateforme.
 *
 * Frameworks suivis : slf4j, lombok_slf4j, log4j2, lombok_log4j2, log4j1, jul, logback,
 * commons_logging, jboss, unknown.
 *
 * Niveaux suivis : error, warn, info, debug, trace.
 */
public class LoggerMetrics implements Metrics {

  // -------------------------------------------------------------------------
  // Domains
  // -------------------------------------------------------------------------

  /** SonarQube dashboard domain for all logger metrics. */
  public static final String DOMAIN = "Logger";

  // -------------------------------------------------------------------------
  // Identifiants et labels de framework — constantes partagées entre les métriques de
  // total, par niveau et de ratio, pour éviter de dupliquer ces littéraux.
  // -------------------------------------------------------------------------

  private static final String ID_SLF4J = "slf4j";
  private static final String ID_LOMBOK_SLF4J = "lombok_slf4j";
  private static final String ID_LOG4J2 = "log4j2";
  private static final String ID_LOMBOK_LOG4J2 = "lombok_log4j2";
  private static final String ID_LOG4J1 = "log4j1";
  private static final String ID_JUL = "jul";
  private static final String ID_LOGBACK = "logback";
  private static final String ID_COMMONS_LOGGING = "commons_logging";
  private static final String ID_JBOSS = "jboss";
  private static final String ID_UNKNOWN = "unknown";

  private static final String LABEL_SLF4J = "SLF4J";
  private static final String LABEL_LOMBOK_SLF4J = "Lombok @Slf4j";
  private static final String LABEL_LOG4J2 = "Log4j2";
  private static final String LABEL_LOMBOK_LOG4J2 = "Lombok @Log4j2";
  private static final String LABEL_LOG4J1 = "Log4j1";
  private static final String LABEL_JUL = "JUL";
  private static final String LABEL_LOGBACK = "Logback";
  private static final String LABEL_COMMONS_LOGGING = "Commons Logging";
  private static final String LABEL_JBOSS = "JBoss Logging";
  private static final String LABEL_UNKNOWN = "Unknown";

  // =========================================================================
  // 1. TOTAL par framework
  // =========================================================================

  public static final Metric<Integer> LOGGER_SLF4J_COUNT = new Metric.Builder(
      "logger_slf4j_count", "SLF4J logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels logger SLF4J (toutes méthodes confondues).")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_LOMBOK_SLF4J_COUNT = new Metric.Builder(
      "logger_lombok_slf4j_count", "Lombok @Slf4j logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels logger générés par l'annotation Lombok @Slf4j.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_LOG4J2_COUNT = new Metric.Builder(
      "logger_log4j2_count", "Log4j2 logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels logger Log4j 2.x.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_LOMBOK_LOG4J2_COUNT = new Metric.Builder(
      "logger_lombok_log4j2_count", "Lombok @Log4j2 logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels logger générés par l'annotation Lombok @Log4j2.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_LOG4J1_COUNT = new Metric.Builder(
      "logger_log4j1_count", "Log4j1 logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels logger Log4j 1.x (legacy).")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_JUL_COUNT = new Metric.Builder(
      "logger_jul_count", "JUL logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels java.util.logging.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_LOGBACK_COUNT = new Metric.Builder(
      "logger_logback_count", "Logback logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels Logback direct (sans façade SLF4J).")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_COMMONS_LOGGING_COUNT = new Metric.Builder(
      "logger_commons_logging_count", "Commons Logging logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels Apache Commons Logging.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_JBOSS_COUNT = new Metric.Builder(
      "logger_jboss_count", "JBoss Logging logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels JBoss Logging.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  public static final Metric<Integer> LOGGER_UNKNOWN_COUNT = new Metric.Builder(
      "logger_unknown_count", "Unknown framework logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total d'appels logger dont le framework n'a pas pu être identifié.")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  // =========================================================================
  // 2. Par framework × par niveau
  //    Convention : logger_<framework>_<level>_count
  //    Niveaux : error, warn, info, debug, trace
  // =========================================================================

  // --- SLF4J ---
  public static final Metric<Integer> LOGGER_SLF4J_ERROR_COUNT = levelMetric(ID_SLF4J, LoggerCounter.LEVEL_ERROR, LABEL_SLF4J);
  public static final Metric<Integer> LOGGER_SLF4J_WARN_COUNT  = levelMetric(ID_SLF4J, LoggerCounter.LEVEL_WARN, LABEL_SLF4J);
  public static final Metric<Integer> LOGGER_SLF4J_INFO_COUNT  = levelMetric(ID_SLF4J, LoggerCounter.LEVEL_INFO, LABEL_SLF4J);
  public static final Metric<Integer> LOGGER_SLF4J_DEBUG_COUNT = levelMetric(ID_SLF4J, LoggerCounter.LEVEL_DEBUG, LABEL_SLF4J);
  public static final Metric<Integer> LOGGER_SLF4J_TRACE_COUNT = levelMetric(ID_SLF4J, LoggerCounter.LEVEL_TRACE, LABEL_SLF4J);

  // --- Lombok @Slf4j ---
  public static final Metric<Integer> LOGGER_LOMBOK_SLF4J_ERROR_COUNT = levelMetric(ID_LOMBOK_SLF4J, LoggerCounter.LEVEL_ERROR, LABEL_LOMBOK_SLF4J);
  public static final Metric<Integer> LOGGER_LOMBOK_SLF4J_WARN_COUNT  = levelMetric(ID_LOMBOK_SLF4J, LoggerCounter.LEVEL_WARN, LABEL_LOMBOK_SLF4J);
  public static final Metric<Integer> LOGGER_LOMBOK_SLF4J_INFO_COUNT  = levelMetric(ID_LOMBOK_SLF4J, LoggerCounter.LEVEL_INFO, LABEL_LOMBOK_SLF4J);
  public static final Metric<Integer> LOGGER_LOMBOK_SLF4J_DEBUG_COUNT = levelMetric(ID_LOMBOK_SLF4J, LoggerCounter.LEVEL_DEBUG, LABEL_LOMBOK_SLF4J);
  public static final Metric<Integer> LOGGER_LOMBOK_SLF4J_TRACE_COUNT = levelMetric(ID_LOMBOK_SLF4J, LoggerCounter.LEVEL_TRACE, LABEL_LOMBOK_SLF4J);

  // --- Log4j2 ---
  public static final Metric<Integer> LOGGER_LOG4J2_ERROR_COUNT = levelMetric(ID_LOG4J2, LoggerCounter.LEVEL_ERROR, LABEL_LOG4J2);
  public static final Metric<Integer> LOGGER_LOG4J2_WARN_COUNT  = levelMetric(ID_LOG4J2, LoggerCounter.LEVEL_WARN, LABEL_LOG4J2);
  public static final Metric<Integer> LOGGER_LOG4J2_INFO_COUNT  = levelMetric(ID_LOG4J2, LoggerCounter.LEVEL_INFO, LABEL_LOG4J2);
  public static final Metric<Integer> LOGGER_LOG4J2_DEBUG_COUNT = levelMetric(ID_LOG4J2, LoggerCounter.LEVEL_DEBUG, LABEL_LOG4J2);
  public static final Metric<Integer> LOGGER_LOG4J2_TRACE_COUNT = levelMetric(ID_LOG4J2, LoggerCounter.LEVEL_TRACE, LABEL_LOG4J2);

  // --- Lombok @Log4j2 ---
  public static final Metric<Integer> LOGGER_LOMBOK_LOG4J2_ERROR_COUNT = levelMetric(ID_LOMBOK_LOG4J2, LoggerCounter.LEVEL_ERROR, LABEL_LOMBOK_LOG4J2);
  public static final Metric<Integer> LOGGER_LOMBOK_LOG4J2_WARN_COUNT  = levelMetric(ID_LOMBOK_LOG4J2, LoggerCounter.LEVEL_WARN, LABEL_LOMBOK_LOG4J2);
  public static final Metric<Integer> LOGGER_LOMBOK_LOG4J2_INFO_COUNT  = levelMetric(ID_LOMBOK_LOG4J2, LoggerCounter.LEVEL_INFO, LABEL_LOMBOK_LOG4J2);
  public static final Metric<Integer> LOGGER_LOMBOK_LOG4J2_DEBUG_COUNT = levelMetric(ID_LOMBOK_LOG4J2, LoggerCounter.LEVEL_DEBUG, LABEL_LOMBOK_LOG4J2);
  public static final Metric<Integer> LOGGER_LOMBOK_LOG4J2_TRACE_COUNT = levelMetric(ID_LOMBOK_LOG4J2, LoggerCounter.LEVEL_TRACE, LABEL_LOMBOK_LOG4J2);

  // --- Log4j1 ---
  public static final Metric<Integer> LOGGER_LOG4J1_ERROR_COUNT = levelMetric(ID_LOG4J1, LoggerCounter.LEVEL_ERROR, LABEL_LOG4J1);
  public static final Metric<Integer> LOGGER_LOG4J1_WARN_COUNT  = levelMetric(ID_LOG4J1, LoggerCounter.LEVEL_WARN, LABEL_LOG4J1);
  public static final Metric<Integer> LOGGER_LOG4J1_INFO_COUNT  = levelMetric(ID_LOG4J1, LoggerCounter.LEVEL_INFO, LABEL_LOG4J1);
  public static final Metric<Integer> LOGGER_LOG4J1_DEBUG_COUNT = levelMetric(ID_LOG4J1, LoggerCounter.LEVEL_DEBUG, LABEL_LOG4J1);
  public static final Metric<Integer> LOGGER_LOG4J1_TRACE_COUNT = levelMetric(ID_LOG4J1, LoggerCounter.LEVEL_TRACE, LABEL_LOG4J1);

  // --- JUL ---
  public static final Metric<Integer> LOGGER_JUL_ERROR_COUNT = levelMetric(ID_JUL, LoggerCounter.LEVEL_ERROR, LABEL_JUL);
  public static final Metric<Integer> LOGGER_JUL_WARN_COUNT  = levelMetric(ID_JUL, LoggerCounter.LEVEL_WARN, LABEL_JUL);
  public static final Metric<Integer> LOGGER_JUL_INFO_COUNT  = levelMetric(ID_JUL, LoggerCounter.LEVEL_INFO, LABEL_JUL);
  public static final Metric<Integer> LOGGER_JUL_DEBUG_COUNT = levelMetric(ID_JUL, LoggerCounter.LEVEL_DEBUG, LABEL_JUL);
  public static final Metric<Integer> LOGGER_JUL_TRACE_COUNT = levelMetric(ID_JUL, LoggerCounter.LEVEL_TRACE, LABEL_JUL);

  // --- Unknown ---
  public static final Metric<Integer> LOGGER_UNKNOWN_ERROR_COUNT = levelMetric(ID_UNKNOWN, LoggerCounter.LEVEL_ERROR, LABEL_UNKNOWN);
  public static final Metric<Integer> LOGGER_UNKNOWN_WARN_COUNT  = levelMetric(ID_UNKNOWN, LoggerCounter.LEVEL_WARN, LABEL_UNKNOWN);
  public static final Metric<Integer> LOGGER_UNKNOWN_INFO_COUNT  = levelMetric(ID_UNKNOWN, LoggerCounter.LEVEL_INFO, LABEL_UNKNOWN);
  public static final Metric<Integer> LOGGER_UNKNOWN_DEBUG_COUNT = levelMetric(ID_UNKNOWN, LoggerCounter.LEVEL_DEBUG, LABEL_UNKNOWN);
  public static final Metric<Integer> LOGGER_UNKNOWN_TRACE_COUNT = levelMetric(ID_UNKNOWN, LoggerCounter.LEVEL_TRACE, LABEL_UNKNOWN);

  // =========================================================================
  // 3. Ratio par framework (% du total des appels logger, 0.0 à 100.0)
  // =========================================================================

  public static final Metric<Double> LOGGER_SLF4J_RATIO = ratioMetric(ID_SLF4J, LABEL_SLF4J);
  public static final Metric<Double> LOGGER_LOMBOK_SLF4J_RATIO = ratioMetric(ID_LOMBOK_SLF4J, LABEL_LOMBOK_SLF4J);
  public static final Metric<Double> LOGGER_LOG4J2_RATIO = ratioMetric(ID_LOG4J2, LABEL_LOG4J2);
  public static final Metric<Double> LOGGER_LOMBOK_LOG4J2_RATIO = ratioMetric(ID_LOMBOK_LOG4J2, LABEL_LOMBOK_LOG4J2);
  public static final Metric<Double> LOGGER_LOG4J1_RATIO = ratioMetric(ID_LOG4J1, LABEL_LOG4J1);
  public static final Metric<Double> LOGGER_JUL_RATIO = ratioMetric(ID_JUL, LABEL_JUL);
  public static final Metric<Double> LOGGER_LOGBACK_RATIO = ratioMetric(ID_LOGBACK, LABEL_LOGBACK);
  public static final Metric<Double> LOGGER_COMMONS_LOGGING_RATIO = ratioMetric(ID_COMMONS_LOGGING, LABEL_COMMONS_LOGGING);
  public static final Metric<Double> LOGGER_JBOSS_RATIO = ratioMetric(ID_JBOSS, LABEL_JBOSS);
  public static final Metric<Double> LOGGER_UNKNOWN_RATIO = ratioMetric(ID_UNKNOWN, LABEL_UNKNOWN);

  // =========================================================================
  // 4. Grand total (tous frameworks confondus)
  // =========================================================================

  public static final Metric<Integer> LOGGER_TOTAL_COUNT = new Metric.Builder(
      "logger_total_count", "Total logger calls", Metric.ValueType.INT)
      .setDescription("Nombre total de tous les appels logger (tous frameworks et niveaux confondus).")
      .setDirection(Metric.DIRECTION_NONE)
      .setQualitative(false)
      .setDomain(DOMAIN)
      .create();

  // =========================================================================
  // Enregistrement des métriques
  // =========================================================================

  @Override
  @SuppressWarnings("rawtypes")
  public List<Metric> getMetrics() {
    return Arrays.asList(
      // totaux par framework
      LOGGER_SLF4J_COUNT, LOGGER_LOMBOK_SLF4J_COUNT,
      LOGGER_LOG4J2_COUNT, LOGGER_LOMBOK_LOG4J2_COUNT,
      LOGGER_LOG4J1_COUNT, LOGGER_JUL_COUNT,
      LOGGER_LOGBACK_COUNT, LOGGER_COMMONS_LOGGING_COUNT,
      LOGGER_JBOSS_COUNT, LOGGER_UNKNOWN_COUNT,

      // par framework × par niveau — SLF4J
      LOGGER_SLF4J_ERROR_COUNT, LOGGER_SLF4J_WARN_COUNT,
      LOGGER_SLF4J_INFO_COUNT,  LOGGER_SLF4J_DEBUG_COUNT, LOGGER_SLF4J_TRACE_COUNT,

      // par framework × par niveau — Lombok @Slf4j
      LOGGER_LOMBOK_SLF4J_ERROR_COUNT, LOGGER_LOMBOK_SLF4J_WARN_COUNT,
      LOGGER_LOMBOK_SLF4J_INFO_COUNT,  LOGGER_LOMBOK_SLF4J_DEBUG_COUNT, LOGGER_LOMBOK_SLF4J_TRACE_COUNT,

      // par framework × par niveau — Log4j2
      LOGGER_LOG4J2_ERROR_COUNT, LOGGER_LOG4J2_WARN_COUNT,
      LOGGER_LOG4J2_INFO_COUNT,  LOGGER_LOG4J2_DEBUG_COUNT, LOGGER_LOG4J2_TRACE_COUNT,

      // par framework × par niveau — Lombok @Log4j2
      LOGGER_LOMBOK_LOG4J2_ERROR_COUNT, LOGGER_LOMBOK_LOG4J2_WARN_COUNT,
      LOGGER_LOMBOK_LOG4J2_INFO_COUNT,  LOGGER_LOMBOK_LOG4J2_DEBUG_COUNT, LOGGER_LOMBOK_LOG4J2_TRACE_COUNT,

      // par framework × par niveau — Log4j1
      LOGGER_LOG4J1_ERROR_COUNT, LOGGER_LOG4J1_WARN_COUNT,
      LOGGER_LOG4J1_INFO_COUNT,  LOGGER_LOG4J1_DEBUG_COUNT, LOGGER_LOG4J1_TRACE_COUNT,

      // par framework × par niveau — JUL
      LOGGER_JUL_ERROR_COUNT, LOGGER_JUL_WARN_COUNT,
      LOGGER_JUL_INFO_COUNT,  LOGGER_JUL_DEBUG_COUNT, LOGGER_JUL_TRACE_COUNT,

      // par framework × par niveau — Unknown
      LOGGER_UNKNOWN_ERROR_COUNT, LOGGER_UNKNOWN_WARN_COUNT,
      LOGGER_UNKNOWN_INFO_COUNT,  LOGGER_UNKNOWN_DEBUG_COUNT, LOGGER_UNKNOWN_TRACE_COUNT,

      // ratios
      LOGGER_SLF4J_RATIO, LOGGER_LOMBOK_SLF4J_RATIO,
      LOGGER_LOG4J2_RATIO, LOGGER_LOMBOK_LOG4J2_RATIO,
      LOGGER_LOG4J1_RATIO, LOGGER_JUL_RATIO,
      LOGGER_LOGBACK_RATIO, LOGGER_COMMONS_LOGGING_RATIO,
      LOGGER_JBOSS_RATIO, LOGGER_UNKNOWN_RATIO,

      // grand total
      LOGGER_TOTAL_COUNT
    );
  }

  // =========================================================================
  // Fabriques privées
  // =========================================================================

  private static Metric<Integer> levelMetric(String framework, String level, String frameworkLabel) {
    String key  = "logger_" + framework + "_" + level + "_count";
    String name = frameworkLabel + " " + level + " calls";
    String desc = "Nombre d'appels " + frameworkLabel + " au niveau " + level + ".";
    return new Metric.Builder(key, name, Metric.ValueType.INT)
        .setDescription(desc)
        .setDirection(Metric.DIRECTION_NONE)
        .setQualitative(false)
        .setDomain(DOMAIN)
        .create();
  }

  private static Metric<Double> ratioMetric(String framework, String frameworkLabel) {
    String key  = "logger_" + framework + "_ratio";
    String name = frameworkLabel + " ratio (%)";
    String desc = "Pourcentage des appels " + frameworkLabel
                + " par rapport au total des appels logger du module/projet.";
    return new Metric.Builder(key, name, Metric.ValueType.FLOAT)
        .setDescription(desc)
        .setDirection(Metric.DIRECTION_NONE)
        .setQualitative(false)
        .setDomain(DOMAIN)
        .create();
  }
}
