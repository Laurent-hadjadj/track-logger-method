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

/**
 * Enum représentant les frameworks de logging supportés.
 *
 * Chaque entrée porte :
 *  - le nom de type totalement qualifié utilisé par le framework
 *  - un label lisible pour les messages d'issue et les métriques
 *
 * Stratégie de détection, par ordre de priorité :
 *  1. Résolution sémantique du type via receiver.symbolType().fullyQualifiedName()
 *  2. Détection d'annotation Lombok sur la classe englobante (@Slf4j, @Log4j2, ...)
 *  3. Repli sur les noms de variable conventionnels (LOGGER, LOG, logger, log)
 */
public enum LoggerFramework {

  // --- SLF4J (utilisé directement ou généré par Lombok @Slf4j / @Slf4jX) ---
  SLF4J(
    "org.slf4j.Logger",
    "SLF4J"
  ),

  // --- Lombok @Slf4j (même type sous-jacent que SLF4J, distingué par l'annotation) ---
  LOMBOK_SLF4J(
    "org.slf4j.Logger",
    "Lombok @Slf4j"
  ),

  // --- Log4j 2.x ---
  LOG4J2(
    "org.apache.logging.log4j.Logger",
    "Log4j2"
  ),

  // --- Lombok @Log4j2 (même type sous-jacent que Log4j2) ---
  LOMBOK_LOG4J2(
    "org.apache.logging.log4j.Logger",
    "Lombok @Log4j2"
  ),

  // --- Log4j 1.x (legacy) ---
  LOG4J1(
    "org.apache.log4j.Logger",
    "Log4j1"
  ),

  // --- Java Util Logging (JUL) ---
  JUL(
    "java.util.logging.Logger",
    "JUL"
  ),

  // --- Logback classic (usage direct, sans façade SLF4J) ---
  LOGBACK(
    "ch.qos.logback.classic.Logger",
    "Logback"
  ),

  // --- Apache Commons Logging ---
  COMMONS_LOGGING(
    "org.apache.commons.logging.Log",
    "Commons Logging"
  ),

  // --- JBoss Logging ---
  JBOSS(
    "org.jboss.logging.Logger",
    "JBoss Logging"
  ),

  // --- Détecté par nom de variable uniquement, framework non résolu ---
  UNKNOWN(
    "unknown",
    "Unknown"
  );

  // -------------------------------------------------------------------------

  private final String fullyQualifiedName;
  private final String label;

  LoggerFramework(String fullyQualifiedName, String label) {
    this.fullyQualifiedName = fullyQualifiedName;
    this.label = label;
  }

  /** Retourne le nom de type totalement qualifié de l'interface de logger de ce framework. */
  public String getFullyQualifiedName() {
    return fullyQualifiedName;
  }

  /** Retourne un label lisible, utilisable dans les messages d'issue et les clés de métrique. */
  public String getLabel() {
    return label;
  }

  // -------------------------------------------------------------------------
  // Fabriques
  // -------------------------------------------------------------------------

  /**
   * Résout le framework uniquement à partir d'un nom de type totalement qualifié, en
   * recherchant parmi les entrées de l'enum celle dont le FQN déclaré correspond — plutôt que
   * de dupliquer une deuxième fois chaque FQN dans un switch, ce que Java interdit de toute
   * façon de faire via une constante partagée avec les arguments des constantes de l'enum.
   * Ne distingue PAS les variantes Lombok (utiliser fromTypeAndAnnotation() pour ça) : SLF4J et
   * LOG4J2 étant déclarés avant leurs variantes Lombok respectives, ce sont eux qui sont
   * retournés en priorité pour un FQN partagé.
   *
   * @param fqn nom de classe totalement qualifié, peut être null
   * @return le framework correspondant, ou UNKNOWN
   */
  public static LoggerFramework fromType(String fqn) {
    if (fqn == null) {
      return UNKNOWN;
    }
    for (LoggerFramework framework : values()) {
      if (framework != UNKNOWN && framework.fullyQualifiedName.equals(fqn)) {
        return framework;
      }
    }
    return UNKNOWN;
  }

  /**
   * Résout le framework à partir d'un nom de type totalement qualifié ET du nom simple d'une
   * annotation Lombok trouvée sur la classe englobante (ex. "Slf4j", "Log4j2").
   *
   * Quand une annotation Lombok est présente et correspond au type résolu, la variante Lombok
   * plus spécifique est retournée à la place de la variante générique.
   *
   * @param fqn              nom de classe totalement qualifié, peut être null
   * @param lombokAnnotation nom simple de l'annotation (sans @), peut être null
   * @return le framework correspondant le plus précis, ou UNKNOWN
   */
  public static LoggerFramework fromTypeAndAnnotation(String fqn, String lombokAnnotation) {
    LoggerFramework base = fromType(fqn);

    if (lombokAnnotation == null) {
      return base;
    }

    return switch (lombokAnnotation) {
      case "Slf4j", "Slf4jX"  -> LOMBOK_SLF4J;
      case "Log4j2"            -> LOMBOK_LOG4J2;
      default                  -> base;
    };
  }
}
