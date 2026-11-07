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
 * Point d'entrée historique du plugin contenant les règles custom.
 */
public class MyJavaRulesPlugin implements Plugin {

  @Override
  public void define(@Nonnull Context context) {
    // Extensions serveur -> instanciées au démarrage du serveur
    context.addExtension(MyJavaRulesDefinition.class);

    // Extensions batch -> instanciées pendant l'analyse du code
    context.addExtension(MyJavaFileCheckRegistrar.class);

  }

}
