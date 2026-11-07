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

import javax.annotation.Nonnull;
import org.sonar.api.Plugin;

/**
 * Point d'entrée du plugin track-logger-method.
 *
 * Extensions enregistrées :
 *  - MyJavaRulesDefinition    — côté serveur : déclare le repository de règles
 *  - LoggerMetrics            — côté serveur : déclare les métriques custom
 *  - MyJavaQualityProfile     — profil qualité "Ma-Moulinette" pré-activé
 *  - LoggerCounter            — côté scanner : compteur partagé (singleton par analyse)
 *  - MyJavaFileCheckRegistrar — batch : enregistre les checks AST et injecte le compteur
 *  - LoggerMetricsSensor      — batch : lit le compteur et publie les métriques
 *
 * Flux de données :
 *   checks AST (par fichier) → increment(framework, level) sur LoggerCounter
 *   → LoggerMetricsSensor publie les métriques
 */
public class MyJavaPlugin implements Plugin {

  @Override
  public void define(@Nonnull Context context) {

    // ── Côté serveur (instanciées au démarrage du serveur) ─────────────────────

    // Déclare le repository de règles et charge les métadonnées JSON
    context.addExtension(MyJavaRulesDefinition.class);

    // Déclare toutes les métriques logger custom (totaux, par niveau, ratios)
    context.addExtension(LoggerMetrics.class);

    // Profil qualité "Ma-Moulinette" avec toutes les règles pré-activées
    context.addExtension(MyJavaQualityProfile.class);

    // ── Côté scanner (instanciées une fois par analyse) ────────────────────────

    // Compteur partagé @ScannerSide — même instance injectée dans les checks ET le sensor
    context.addExtension(LoggerCounter.class);

    // Enregistre les checks AST et injecte le LoggerCounter dans chacun
    context.addExtension(MyJavaFileCheckRegistrar.class);

    // Sensor post-analyse : lit le LoggerCounter et publie les métriques
    context.addExtension(LoggerMetricsSensor.class);
  }
}
